package org.ton.tac

import org.ton.bytecode.TvmArrayStackEntryDescription
import org.ton.bytecode.TvmConstStackEntryDescription
import org.ton.bytecode.TvmContBasicJmpxInst
import org.ton.bytecode.TvmContBasicRetaltInst
import org.ton.bytecode.TvmContConditionalIfnotretaltInst
import org.ton.bytecode.TvmContConditionalIfretaltInst
import org.ton.bytecode.TvmContDictCalldictInst
import org.ton.bytecode.TvmContDictCalldictLongInst
import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmControlFlowContinuation
import org.ton.bytecode.TvmDictPrefixPfxdictconstgetjmpInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetexecInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetjmpInst
import org.ton.bytecode.TvmDictSpecialDictigetexecInst
import org.ton.bytecode.TvmDictSpecialDictigetexeczInst
import org.ton.bytecode.TvmDictSpecialDictigetjmpInst
import org.ton.bytecode.TvmDictSpecialDictigetjmpzInst
import org.ton.bytecode.TvmDictSpecialDictugetexecInst
import org.ton.bytecode.TvmDictSpecialDictugetexeczInst
import org.ton.bytecode.TvmDictSpecialDictugetjmpInst
import org.ton.bytecode.TvmDictSpecialDictugetjmpzInst
import org.ton.bytecode.TvmDisasmCodeBlock
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmRealInst
import org.ton.bytecode.TvmSimpleStackEntryDescription

internal fun <Inst : AbstractTacInst> generateTacCodeBlock(
    ctx: TacGenerationContext<Inst>,
    codeBlock: TvmDisasmCodeBlock,
    stack: Stack = Stack(emptyList()),
    endingInstGenerator: EndingInstGenerator<Inst> = ReturnInstGenerator(),
    registerState: RegisterState = RegisterState(),
): TacContinuationInfo<Inst> {
    val tacInstructions = mutableListOf<Inst>()

    var noExit = false

    for (inst in codeBlock.instList) {
        check(inst is TvmRealInst) {
            "Unexpected artificial instruction: $inst"
        }

        val curInstructions = processInstruction(ctx, stack, inst, endingInstGenerator, registerState)
        tacInstructions += curInstructions

        if (inst is TvmContBasicJmpxInst) noExit = true

        // like THROW
        if (!inst.noBranch && inst.branches.isEmpty()) {
            noExit = true
            break
        }
    }

    if (!noExit) {
        tacInstructions += endingInstGenerator.generateEndingInst(ctx, stack)
    }

    val numberOfResultElements =
        if (!noExit) {
            stack.size
        } else {
            null
        }

    val methodArgs =
        stack.getCreatedArgs().map {
            TacVar(
                name = it.name,
                valueTypes = it.valueTypes,
            )
        }

    return TacContinuationInfo(
        instructions = tacInstructions,
        methodArgs = methodArgs,
        numberOfReturnedValues = numberOfResultElements,
        originalTvmCode = codeBlock,
    )
}

private fun <Inst : AbstractTacInst> processInstruction(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    inst: TvmRealInst,
    endingInstGenerator: EndingInstGenerator<Inst>,
    registerState: RegisterState,
): List<Inst> {
    throwErrorIfStackTypesNotSupported(inst)

    val isBranching = inst.branches.isNotEmpty() && !inst.ignoreBranches()

    if (isBranching) {
        val result = handleBranchingInstruction(ctx, stack, inst, endingInstGenerator, registerState)

        return result
    } else {
        val handler = TacHandlerRegistry.getHandler(inst)
        val rawInstructions = handler.handle(ctx, stack, inst, registerState)

        return rawInstructions.map { rawInst ->
            wrapInst(ctx, stack, rawInst)
        }
    }
}

private fun <Inst : AbstractTacInst> wrapInst(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    inst: TacInst,
): Inst {
    @Suppress("UNCHECKED_CAST")
    return if (ctx.debug) {
        val stackAfter = stack.copyEntries()
        TacInstDebugWrapper(inst, stackAfter) as Inst
    } else {
        inst as Inst
    }
}

private fun <Inst : AbstractTacInst> handleBranchingInstruction(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    inst: TvmRealInst,
    endingInstGenerator: EndingInstGenerator<Inst>,
    registerState: RegisterState,
): List<Inst> {
    throwErrorIfBranchesNotTypeVar(inst)

    val inputsSpec = inst.stackInputs ?: emptyList()
    val inputsWithNames = mutableListOf<Pair<String, TacStackValue>>()

    inputsSpec.reversed().forEach { spec ->
        val value = stack.pop(0)

        val name =
            when (spec) {
                is TvmSimpleStackEntryDescription -> spec.name
                is TvmArrayStackEntryDescription -> spec.name
                is TvmConstStackEntryDescription -> "const"
                else -> "unknown"
            }

        inputsWithNames.add(0, name to value)
    }

    val outputs = mutableListOf<TacStackValue>()

    val saveC0 = validateBranchStructure(inst)

    val stackContinuationMap =
        inputsWithNames
            .mapNotNull { (name, value) ->
                if (value is ContinuationValue) {
                    name to value.continuationRef
                } else {
                    null
                }
            }.toMap()

    val operandContinuationInfo = if (inst is TvmContOperandInst) extractOperandContinuations(ctx, inst) else null

    val continuationAnalysis = analyzeContinuations(ctx, inst, operandContinuationInfo, stackContinuationMap, stack)
    val controlFlowPrep = prepareControlFlow(ctx, continuationAnalysis, saveC0, inst, endingInstGenerator)

    return generateControlFlowInstructions(
        ctx,
        stack,
        inst,
        inst.operands,
        inputsWithNames,
        continuationAnalysis,
        controlFlowPrep,
        registerState,
        outputs,
    )
}

/**
 * Checks that save structure is the following:
 *  {
 *     "type": "variable",
 *     "save": {
 *       "c0": {
 *         "type": "cc",
 *         "save": { "c0": { "type": "register", "index": 0 } }
 *       }
 *     }
 *   }
 * */
private fun TvmControlFlowContinuation.hasStandardC0Save(): Boolean {
    val castedSave =
        save
            ?: return false
    if (castedSave.size != 1) {
        return false
    }
    val entry = castedSave.entries.single()
    if (entry.key != "c0") {
        return false
    }
    val value = entry.value
    if (value.type != "cc") {
        return false
    }
    val valueSave =
        value.save
            ?: return false
    if (valueSave.size != 1) {
        return false
    }
    val valueSaveEntry = valueSave.entries.single()
    if (valueSaveEntry.key != "c0") {
        return false
    }
    // TODO: also check index
    if (valueSaveEntry.value.type != "register") {
        return false
    }
    return true
}

// special cases
private fun TvmInst.ignoreBranches(): Boolean =
    when (this) {
        is TvmContDictCalldictInst,
        is TvmContDictCalldictLongInst,
        -> true

        is TvmDictSpecialDictugetexecInst,
        is TvmDictSpecialDictugetexeczInst,
        is TvmDictSpecialDictugetjmpInst,
        is TvmDictSpecialDictugetjmpzInst,

        is TvmDictSpecialDictigetexecInst,
        is TvmDictSpecialDictigetexeczInst,
        is TvmDictSpecialDictigetjmpInst,
        is TvmDictSpecialDictigetjmpzInst,
        -> true

        is TvmDictPrefixPfxdictgetjmpInst,
        is TvmDictPrefixPfxdictgetexecInst,
        is TvmDictPrefixPfxdictconstgetjmpInst,
        -> true

        else -> false
    }

private data class ContinuationAnalysis<Inst : AbstractTacInst>(
    val continuationInfos: List<TacContinuationInfo<Inst>>,
    val stackEffects: Set<Int>,
    val maxArguments: Int,
    val maxResultValues: Int,
)

private data class ControlFlowPreparation<Inst : AbstractTacInst>(
    val outputVars: List<TacVar>,
    val newEndInstGenerator: EndingInstGenerator<Inst>,
    val label: String?,
    val inputVars: List<TacVar>,
)

private fun validateBranchStructure(inst: TvmRealInst): Boolean {
    val (haveStandardC0, haveNoSave) =
        inst.branches
            .map {
                it.hasStandardC0Save() to (it.save?.isNotEmpty() != true)
            }.unzip()

    check(haveStandardC0.toSet().size == 1) {
        "All branches must either all have standard c0 structure, or all don't have it"
    }

    val saveC0 = haveStandardC0.first()

    check(haveNoSave.toSet().size == 1) {
        "All branches must either all not have save, or all have them"
    }

    val noSave = haveNoSave.first()

    if (!saveC0 && !noSave) {
        TODO("Control flow of instruction ${inst.mnemonic} not supported")
    }

    return saveC0
}

private fun <Inst : AbstractTacInst> createReturnBlock(
    isAlt: Boolean,
    stackEntries: List<TacStackValue>,
): TacContinuationInfo<Inst> {
    val instruction =
        if (isAlt) {
            @Suppress("UNCHECKED_CAST")
            TacOrdinaryInst<Inst>(
                mnemonic = "RETALT",
                operands = emptyMap(),
                inputs = stackEntries,
                outputs = emptyList(),
                blocks = emptyList(),
            ) as Inst
        } else {
            @Suppress("UNCHECKED_CAST")
            TacReturnInst(
                result = stackEntries,
            ) as Inst
        }

    return TacContinuationInfo(
        instructions = listOf(instruction),
        methodArgs = emptyList(),
        numberOfReturnedValues = stackEntries.size,
        originalTvmCode = org.ton.bytecode.TvmInlineBlock(mutableListOf()),
    )
}

private fun <Inst : AbstractTacInst> createUnknownContinuationInfo(): TacContinuationInfo<Inst> {
    @Suppress("UNCHECKED_CAST")
    val unknownInst =
        TacOrdinaryInst<Inst>(
            mnemonic = "UNKNOWN_DYNAMIC_JUMP",
            operands = emptyMap(),
            inputs = emptyList(),
            outputs = emptyList(),
            blocks = emptyList(),
        ) as Inst

    return TacContinuationInfo(
        instructions = listOf(unknownInst),
        methodArgs = emptyList(),
        numberOfReturnedValues = 0,
        originalTvmCode = org.ton.bytecode.TvmInlineBlock(mutableListOf()),
    )
}

private fun <Inst : AbstractTacInst> analyzeContinuations(
    ctx: TacGenerationContext<Inst>,
    inst: TvmRealInst,
    operandContinuationInfo: OperandContinuationInfo?,
    stackContinuationMap: Map<String, ContinuationId>,
    stack: Stack,
): ContinuationAnalysis<Inst> {
    val continuationMap =
        operandContinuationInfo?.let {
            it.operandContinuationIds + stackContinuationMap
        } ?: stackContinuationMap
    val controlFlowContinuationIds =
        inst.branches.map { branch ->
            when (branch.type) {
                "variable" -> continuationMap[branch.variableName] ?: -1 // Unknown Code
                "register" -> {
                    if (inst is TvmContBasicRetaltInst ||
                        inst is TvmContConditionalIfretaltInst ||
                        inst is TvmContConditionalIfnotretaltInst
                    ) {
                        -11
                    } else {
                        -10
                    }
                }
                else -> -1
            }
        }

    val currentStackEntries = stack.copyEntries()

    val continuationInfos =
        controlFlowContinuationIds.map { ref ->
            when (ref) {
                -1 -> createUnknownContinuationInfo()
                -10 -> createReturnBlock(isAlt = false, stackEntries = currentStackEntries)
                -11 -> createReturnBlock(isAlt = true, stackEntries = currentStackEntries)
                else ->
                    ctx.isolatedContinuations[ref]
                        ?: error("Continuation with label $ref not found")
            }
        }

    val stackEffects =
        continuationInfos
            .mapNotNull {
                it.numberOfReturnedValues?.let { numberOfReturnedValues ->
                    numberOfReturnedValues - it.methodArgs.size
                }
            }.toSet()

    if (stackEffects.isEmpty()) {
        TODO("Case when all branches of instruction fail")
    }
    check(stackEffects.size == 1) {
        "Cannot build three address code if stack effects are different"
    }

    val maxArguments = continuationInfos.maxOf { it.methodArgs.size }

    val maxResultValues = continuationInfos.mapNotNull { it.numberOfReturnedValues }.max()

    return ContinuationAnalysis(
        continuationInfos = continuationInfos,
        stackEffects = stackEffects,
        maxArguments = maxArguments,
        maxResultValues = maxResultValues,
    )
}

private fun <Inst : AbstractTacInst> prepareControlFlow(
    ctx: TacGenerationContext<Inst>,
    continuationAnalysis: ContinuationAnalysis<Inst>,
    saveC0: Boolean,
    inst: TvmRealInst,
    endingInstGenerator: EndingInstGenerator<Inst>,
): ControlFlowPreparation<Inst> {
    val inputVars =
        if (saveC0) {
            List(continuationAnalysis.maxArguments) {
                TacVar(ctx.nextVarName())
            }
        } else {
            emptyList()
        }

    val outputVars =
        if (!saveC0) {
            emptyList()
        } else if (inst.noBranch) {
            check(continuationAnalysis.stackEffects.single() == 0) {
                "If [saveC0] and [noBranch], stack effect must be zero, but it is ${continuationAnalysis.stackEffects.single()}. Instruction ${inst.mnemonic}"
            }
            inputVars
        } else if (!inst.noBranch) {
            List(continuationAnalysis.maxResultValues) {
                TacVar(ctx.nextVarName())
            }
        } else {
            error("Unexpected control flow in ${inst.mnemonic}")
        }

    val (newEndInstGenerator, label) =
        if (saveC0) {
            val label = ctx.nextLabel()

            GotoInstGenerator<Inst>(outputVars, label) to label
        } else {
            endingInstGenerator to null
        }

    return ControlFlowPreparation(
        outputVars = outputVars,
        newEndInstGenerator = newEndInstGenerator,
        label = label,
        inputVars = inputVars,
    )
}

private fun <Inst : AbstractTacInst> generateControlFlowInstructions(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    inst: TvmRealInst,
    operands: Map<String, Any?>,
    inputs: List<Pair<String, TacStackValue>>,
    continuationAnalysis: ContinuationAnalysis<Inst>,
    controlFlowPrep: ControlFlowPreparation<Inst>,
    registerState: RegisterState,
    outputs: List<TacStackValue>,
): MutableList<Inst> {
    val result = mutableListOf<Inst>()

    controlFlowPrep.inputVars.forEach { inputVar ->
        val assignInst = stack.getAssignInst(inputVar, controlFlowPrep.inputVars.size - 1)

        result += wrapInst(ctx, stack, assignInst)
    }

    val branchStates =
        continuationAnalysis.continuationInfos.map {
            Pair(stack.copy(), registerState.copy())
        }

    val noOpGenerator =
        object : EndingInstGenerator<Inst> {
            override fun generateEndingInst(
                ctx: TacGenerationContext<Inst>,
                stack: Stack,
            ): List<Inst> = emptyList()
        }

    val blockInfos =
        continuationAnalysis.continuationInfos.mapIndexed { index, continuationInfo ->
            val (branchStack, branchRegisterState) = branchStates[index]
            generateTacCodeBlock(
                ctx,
                codeBlock = continuationInfo.originalTvmCode,
                stack = branchStack,
                endingInstGenerator = noOpGenerator,
                registerState = branchRegisterState,
            )
        }

    if (branchStates.size > 1) {
        val (firstStack, firstRegisterState) = branchStates.first()
        val allStatesCompatibleResults =
            branchStates.drop(1).map { (otherStack, otherRegisterState) ->
                val (regMsg, regOk) = areStatesCompatible(firstRegisterState, otherRegisterState)
                if (!regOk) return@map regMsg to false

                val (stackMsg, stackOk) = areStacksCompatible(firstStack, otherStack)
                if (!stackOk) return@map stackMsg to false

                "Compatible" to true
            }

        val allStatesCompatible = allStatesCompatibleResults.all { it.second }

        if (!allStatesCompatible) {
            val errorMessages =
                allStatesCompatibleResults
                    .filter { !it.second }
                    .joinToString("\n") { it.first }

            throw IllegalStateException(
                "Decompilation failed at '${inst.mnemonic}': Incompatible states after branches merging at label '${controlFlowPrep.label}':\n$errorMessages",
            )
        }
    }

    val blocks =
        blockInfos.mapIndexed { index, info ->
            val (branchStack, _) = branchStates[index]
            val endingInsts = controlFlowPrep.newEndInstGenerator.generateEndingInst(ctx, branchStack)
            info.instructions + endingInsts
        }

    if (branchStates.isNotEmpty()) {
        val (_, representativeRegisterState) = branchStates.first()
        registerState.assignFrom(representativeRegisterState)
    }

    controlFlowPrep.inputVars.forEach { _ -> stack.pop(0) }
    controlFlowPrep.outputVars.forEach { stack.push(it) }

    val controlFlowInputNames = inst.branches.mapNotNull { it.variableName }
    val filteredInputs = inputs.filter { it.first !in controlFlowInputNames }.map { it.second }

    val controlFlowInst =
        TacOrdinaryInst(
            mnemonic = inst.mnemonic,
            operands = operands,
            inputs = filteredInputs,
            outputs = outputs,
            blocks = blocks,
        )

    result += wrapInst(ctx, stack, controlFlowInst)

    if (controlFlowPrep.label != null) {
        val labelInst = TacLabel(controlFlowPrep.label)

        result += wrapInst(ctx, stack, labelInst)
    }

    return result
}

private fun <Inst : AbstractTacInst> generateTacContractCodeInternal(
    contract: TvmContractCode,
    debug: Boolean,
): TacContractCode<Inst> {
    val ctx = TacGenerationContext<Inst>(contract, debug = debug)

    val (mainInstructions, mainArgs) = generateTacCodeBlock(ctx, codeBlock = contract.mainMethod)
    val main =
        TacMainMethod(
            instructions = mainInstructions,
            methodArgs = mainArgs,
        )

    val methods =
        contract.methods.mapValues { (id, method) ->
            val methodStack = Stack(emptyList())
            val (insts, methodArgs) = generateTacCodeBlock(ctx, codeBlock = method, stack = methodStack)

            TacMethod(
                methodId = id,
                instructions = insts,
                methodArgs = methodArgs,
            )
        }

    return TacContractCode(
        mainMethod = main,
        methods = methods,
    )
}

fun generateDebugTacContractCode(contract: TvmContractCode): TacContractCode<TacDebugInst> =
    generateTacContractCodeInternal(contract, debug = true)

fun generateTacContractCode(contract: TvmContractCode): TacContractCode<TacInst> =
    generateTacContractCodeInternal(contract, debug = false)
