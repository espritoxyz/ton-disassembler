package org.ton.tac

import org.ton.bytecode.TvmAppGlobalSetglobInst
import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContRegistersPopctrInst
import org.ton.bytecode.TvmContRegistersPushctrInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmControlFlowContinuation
import org.ton.bytecode.TvmDictSpecialDictigetjmpzInst
import org.ton.bytecode.TvmDisasmCodeBlock
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmRealInst
import org.ton.bytecode.TvmStackBasicInst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.TvmType
import org.ton.bytecode.extractPrimitiveOperands

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

    if (inst is TvmStackBasicInst || inst is TvmStackComplexInst) {
        stack.execStackInstruction(inst)

        return if (ctx.debug) {
            val stateAfter = stack.copyEntries()
            return TacDebugStackInst(
                mnemonic = inst.mnemonic,
                parameters = extractPrimitiveOperands(inst),
                stackAfter = stateAfter,
            ).let {
                @Suppress("unchecked_cast")
                listOf(it as Inst)
            }
        } else {
            emptyList()
        }
    }

    if (inst.mnemonic in CALLDICT_MNEMONICS) {
        val operands = extractPrimitiveOperands(inst).toMutableMap()

        var methodNumber = operands["n"] ?: error("Missing method number in CALLDICT")
        methodNumber = methodNumber as? Int
            ?: error("Expected method number to be Int in CALLDICT, but it is: ${methodNumber::class.simpleName}")
        methodNumber = methodNumber.toBigInteger()

        val callInst = processCallDict(ctx, stack, methodNumber, inst, operands)

        val resultInst =
            if (ctx.debug) {
                val stackAfter = stack.copyEntries()
                TacInstDebugWrapper(callInst, stackAfter)
            } else {
                callInst
            }

        @Suppress("unchecked_cast")
        return listOf(resultInst as Inst)
    }

    return processOrdinaryInst(ctx, stack, inst, endingInstGenerator, registerState)
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
private fun TvmInst.ignoreBranches(): Boolean = this is TvmDictSpecialDictigetjmpzInst

private fun <Inst : AbstractTacInst> processOrdinaryInst(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    inst: TvmRealInst,
    endingInstGenerator: EndingInstGenerator<Inst>,
    registerState: RegisterState,
): List<Inst> {
    when (inst) {
        is TvmAppGlobalSetglobInst -> {
            val value = stack.pop(0)
            val globalName = "global_${inst.k}"

            if (value is TacTupleValue) registerState.tupleRegistry[globalName] = value.elements

            return emptyList()
        }
        is TvmContRegistersPopctrInst -> {
            val registerValue =
                when (val poppedValue = stack.pop(0)) {
                    is ContinuationValue ->
                        ControlRegisterValue.ContinuationRegisterValue(
                            ref = poppedValue.continuationRef,
                        )
                    is TacVar -> {
                        val type = poppedValue.valueTypes.firstOrNull() ?: error("Incorrect value")
                        when (type) {
                            TvmType.CELL -> ControlRegisterValue.CellRegisterValue()
                            TvmType.INT -> ControlRegisterValue.IntegerRegisterValue()
                            TvmType.SLICE -> ControlRegisterValue.SliceRegisterValue()
                            else -> error("Unsupported type: $type")
                        }
                    }
                    is TacTupleValue -> ControlRegisterValue.TupleRegisterValue()
                }
            registerState.controlRegisters[inst.i] = registerValue
            return emptyList()
        }
        is TvmContRegistersPushctrInst -> {
            val registerValue = registerState.controlRegisters[inst.i] ?: ControlRegisterValue.CellRegisterValue()

            val pushValue =
                when (registerValue) {
                    is ControlRegisterValue.ContinuationRegisterValue ->
                        ContinuationValue(
                            "ctr_${inst.i}",
                            registerValue.ref,
                        )
                    is ControlRegisterValue.CellRegisterValue ->
                        TacVar(
                            name = "ctr_${inst.i}",
                            valueTypes = listOf(TvmType.CELL),
                        )
                    is ControlRegisterValue.IntegerRegisterValue ->
                        TacVar(
                            name = "ctr_${inst.i}",
                            valueTypes = listOf(TvmType.INT),
                        )
                    is ControlRegisterValue.SliceRegisterValue ->
                        TacVar(
                            name = "ctr_${inst.i}",
                            valueTypes = listOf(TvmType.SLICE),
                        )
                    is ControlRegisterValue.TupleRegisterValue ->
                        TacVar(
                            name = "ctr_${inst.i}",
                            valueTypes = listOf(TvmType.TUPLE),
                        )
                }
            stack.push(pushValue)
            return emptyList()
        }
        else -> {
            val operands = extractPrimitiveOperands(inst)

            val specInputs = inst.stackInputs ?: emptyList()
            val specOutputs = inst.stackOutputs ?: emptyList()

            val operandContinuationInfo =
                if (inst is TvmContOperandInst) {
                    extractOperandContinuations(ctx, inst)
                } else {
                    null
                }

            val (inputs, outputs, stackContinuationMap) =
                stack.processNonStackInst(
                    ctx,
                    inputSpec = specInputs,
                    outputSpec = specOutputs,
                    contRef = operandContinuationInfo?.resultContinuationId,
                    registerState = registerState,
                )

            if (inst.branches.isEmpty() || inst.ignoreBranches()) {
                val tacInst =
                    TacOrdinaryInst<Inst>(
                        mnemonic = inst.mnemonic,
                        operands = operands,
                        inputs = inputs.map { it.second },
                        outputs = outputs,
                        blocks = emptyList(),
                    )

                val result =
                    if (ctx.debug) {
                        val stackAfter = stack.copyEntries()
                        TacInstDebugWrapper(tacInst, stackAfter)
                    } else {
                        tacInst
                    }

                @Suppress("unchecked_cast")
                return listOf(result as Inst)
            }

            val continuationMap =
                operandContinuationInfo?.let {
                    it.operandContinuationIds + stackContinuationMap
                } ?: stackContinuationMap

            throwErrorIfBranchesNotTypeVar(inst)

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

            val inputVars =
                if (saveC0) {
                    List(maxArguments) {
                        TacVar(ctx.nextVarName())
                    }
                } else {
                    emptyList()
                }

            val outputVars =
                if (!saveC0) {
                    emptyList()
                } else if (inst.noBranch) {
                    check(stackEffects.single() == 0) {
                        "If [saveC0] and [noBranch], stack effect must be zero, but it is ${stackEffects.single()}. Instruction ${inst.mnemonic}"
                    }
                    inputVars
                } else if (!inst.noBranch) {
                    List(maxResultValues) {
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

            val result = mutableListOf<Inst>()

            inputVars.forEach { inputVar ->
                val assignInst = stack.getAssignInst(inputVar, inputVars.size - 1)
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

            val blocks =
                continuationInfos.map { continuationInfo ->
                    generateTacCodeBlock(
                        ctx,
                        codeBlock = continuationInfo.originalTvmCode,
                        stack = stack.copy(),
                        endingInstGenerator = newEndInstGenerator,
                        registerState = registerState.copy(),
                    )
                }

            inputVars.forEach { _ ->
                stack.pop(0)
            }

            outputVars.forEach {
                stack.push(it)
            }

            val controlFlowInputNames = inst.branches.mapNotNull { it.variableName }

            val controlFlowInst =
                TacOrdinaryInst(
                    mnemonic = inst.mnemonic,
                    operands = operands,
                    inputs = inputs.filter { it.first !in controlFlowInputNames }.map { it.second },
                    outputs = outputs,
                    blocks = blocks.map { it.instructions },
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

            if (label != null) {
                val labelInst = TacLabel(label)
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
    }
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
