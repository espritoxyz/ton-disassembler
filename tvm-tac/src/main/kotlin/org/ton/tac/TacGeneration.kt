package org.ton.tac

import org.ton.bytecode.TvmArrayStackEntryDescription
import org.ton.bytecode.TvmConstStackEntryDescription
import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmControlFlowContinuation
import org.ton.bytecode.TvmDictSpecialDictigetjmpzInst
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

        if (inst.mnemonic == "JMPX") noExit = true

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

        @Suppress("UNCHECKED_CAST")
        return result
    } else {
        val handler = TacHandlerRegistry.getHandler(inst)
        val rawInstructions = handler.handle(ctx, stack, inst, registerState)

        @Suppress("UNCHECKED_CAST")
        return rawInstructions.map { rawInst ->
            wrapInst(ctx, stack, rawInst)
        }
    }
}

private fun <Inst : AbstractTacInst> wrapInst(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    inst: AbstractTacInst,
): Inst {
    @Suppress("UNCHECKED_CAST")
    return if (ctx.debug) {
        val stackAfter = stack.copyEntries()
        TacInstDebugWrapper(inst as TacInst, stackAfter) as Inst
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

    val continuationAnalysis = analyzeContinuations(ctx, inst, operandContinuationInfo, stackContinuationMap)
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
    this is TvmDictSpecialDictigetjmpzInst ||
        this.mnemonic == "CALLDICT" ||
        this.mnemonic == "CALLDICT_LONG"

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

private fun <Inst : AbstractTacInst> analyzeContinuations(
    ctx: TacGenerationContext<Inst>,
    inst: TvmRealInst,
    operandContinuationInfo: OperandContinuationInfo?,
    stackContinuationMap: Map<String, ContinuationId>,
): ContinuationAnalysis<Inst> {
    val continuationMap =
        operandContinuationInfo?.let {
            it.operandContinuationIds + stackContinuationMap
        } ?: stackContinuationMap
    val controlFlowContinuationIds =
        inst.branches.map {
            continuationMap[it.variableName]
                ?: error("Continuation of name ${it.variableName} not found.")
        }

    val continuationInfos =
        controlFlowContinuationIds.map { ref ->
            ctx.isolatedContinuations[ref]
                ?: error("Continuation with label $ref not found")
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

fun tupleCompatible(
    val1: TacTupleValue,
    val2: TacStackValue,
): Boolean {
    val tuple2 = val2 as TacTupleValue
    if (val1.elements.size != tuple2.elements.size) return false

    for (i in val1.elements.indices) {
        if (!isCompatible(val1.elements[i], tuple2.elements[i])) {
            return false
        }
    }
    return true
}

fun isCompatible(
    val1: TacStackValue,
    val2: TacStackValue,
): Boolean {
    if (val1::class != val2::class) return false

    return when (val1) {
        is TacVar -> val1.valueTypes == (val2 as TacVar).valueTypes
        is TacIntValue -> val1.valueTypes == (val2 as TacVar).valueTypes
        is TacTupleValue -> tupleCompatible(val1, val2)
        is ContinuationValue ->
            val1.valueTypes == (val2 as ContinuationValue).valueTypes &&
                val1.continuationRef == val2.continuationRef
    }
}

/**
 * Checks if two execution states (registers and tuple definitions) are compatible
 * to be merged after branching.
 *
 * @return A Pair where:
 *         - first: A description of the incompatibility (error message) or "States are compatible".
 *         - second: Boolean indicating success (true if compatible, false otherwise).
 */
fun areStatesCompatible(
    state1: RegisterState,
    state2: RegisterState,
): Pair<String, Boolean> {
    val regKeys1 = state1.controlRegisters.keys
    val regKeys2 = state2.controlRegisters.keys
    if (regKeys1 != regKeys2) {
        val missingIn1 = regKeys2 - regKeys1
        val missingIn2 = regKeys1 - regKeys2
        val errorString =
            buildString {
                append("Control register keys mismatch: ")
                if (missingIn1.isNotEmpty()) append("missing in state1: $missingIn1")
                if (missingIn1.isNotEmpty() && missingIn2.isNotEmpty()) append("; ")
                if (missingIn2.isNotEmpty()) append("missing in state2: $missingIn2")
            }
        return Pair(errorString, false)
    }

    for (key in regKeys1) {
        val value1 = state1.controlRegisters.getValue(key)
        val value2 = state2.controlRegisters.getValue(key)
        if (!isCompatible(value1, value2)) {
            val errorString =
                "Conflict in control register c_$key: " +
                    "state1 has $value1 (types: ${value1.valueTypes}), " +
                    "state2 has $value2 (types: ${value2.valueTypes})"
            return Pair(errorString, false)
        }
    }

    val globalKeys1 = state1.globalVariables.keys
    val globalKeys2 = state2.globalVariables.keys
    if (globalKeys1 != globalKeys2) {
        if (globalKeys1 != globalKeys2) {
            val missingIn1 = globalKeys2 - globalKeys1
            val missingIn2 = globalKeys1 - globalKeys2
            val errorString =
                buildString {
                    append("Global variable keys mismatch: ")
                    if (missingIn1.isNotEmpty()) append("missing in state1 indexes: $missingIn1")
                    if (missingIn1.isNotEmpty() && missingIn2.isNotEmpty()) append("; ")
                    if (missingIn2.isNotEmpty()) append("missing in state2 indexes: $missingIn2")
                }
            return Pair(errorString, false)
        }
    }

    for (key in globalKeys1) {
        val val1 = state1.globalVariables.getValue(key)
        val val2 = state2.globalVariables.getValue(key)

        if (!isCompatible(val1, val2)) {
            if (val1 is TacTupleValue && val2 is TacTupleValue) {
                if (val1.elements.size != val2.elements.size) {
                    return "Global #$key tuple size mismatch: ${val1.elements.size} vs ${val2.elements.size}" to false
                }

                for (i in val1.elements.indices) {
                    val elem1 = val1.elements[i]
                    val elem2 = val2.elements[i]
                    if (!isCompatible(elem1, elem2)) {
                        return "Global $key tuple element mismatch at index $i: " +
                            "state1 has $elem1 (types: ${elem1.valueTypes}), " +
                            "state2 has $elem2 (types: ${elem2.valueTypes})" to false
                    }
                }
            }

            val errorString =
                "Conflict in global variable $key: " +
                    "state1 has $val1 (types: ${val1.valueTypes}), " +
                    "state2 has $val2 (types: ${val2.valueTypes})"
            return Pair(errorString, false)
        }
    }

    return Pair("States are compatible", true)
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
        val newInst =
            if (ctx.debug) {
                val stackAfter = stack.copyEntries()
                TacInstDebugWrapper(assignInst, stackAfter)
            } else {
                assignInst
            }

        @Suppress("unchecked_cast")
        result += newInst as Inst
    }

    val branchStates =
        continuationAnalysis.continuationInfos.map {
            Pair(stack.copy(), registerState.copy())
        }

    val blocks =
        continuationAnalysis.continuationInfos.mapIndexed { index, continuationInfo ->
            val (branchStack, branchRegisterState) = branchStates[index]

            generateTacCodeBlock(
                ctx,
                codeBlock = continuationInfo.originalTvmCode,
                stack = branchStack,
                endingInstGenerator = controlFlowPrep.newEndInstGenerator,
                registerState = branchRegisterState,
            ).instructions
        }

    if (branchStates.size > 1) {
        val (_, firstRegisterState) = branchStates.first()
        val allStatesCompatibleResults =
            branchStates.drop(1).map { (_, otherRegisterState) ->
                areStatesCompatible(firstRegisterState, otherRegisterState)
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

    if (branchStates.isNotEmpty()) {
        val (_, representativeRegisterState) = branchStates.first()
        registerState.assignFrom(representativeRegisterState)
    }

    controlFlowPrep.inputVars.forEach { _ ->
        stack.pop(0)
    }

    controlFlowPrep.outputVars.forEach {
        stack.push(it)
    }

    val controlFlowInputNames = inst.branches.mapNotNull { it.variableName }

    val controlFlowInst =
        TacOrdinaryInst(
            mnemonic = inst.mnemonic,
            operands = operands,
            inputs = inputs.filter { it.first !in controlFlowInputNames }.map { it.second },
            outputs = outputs,
            blocks = blocks,
        )

    val resultInst =
        if (ctx.debug) {
            val stackAfter = stack.copyEntries()
            TacInstDebugWrapper(controlFlowInst, stackAfter)
        } else {
            controlFlowInst
        }

    @Suppress("unchecked_cast")
    result += resultInst as Inst

    if (controlFlowPrep.label != null) {
        val labelInst = TacLabel(controlFlowPrep.label)
        val resultLabelInst =
            if (ctx.debug) {
                val stackAfter = stack.copyEntries()
                TacInstDebugWrapper(labelInst, stackAfter)
            } else {
                labelInst
            }

        @Suppress("unchecked_cast")
        result += resultLabelInst as Inst
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
