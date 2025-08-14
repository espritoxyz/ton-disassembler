package org.ton.tac

import org.ton.bytecode.TvmConstDataInst
import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmDisasmCodeBlock
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmStackBasicInst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.extractPrimitiveOperands

data class ContGeneratedInfo(
    val instructions: MutableList<AbstractTacInst>,
    val arguments: List<TacVar>,
)

fun generateTacCodeBlock(
    ctx: TacGenerationContext,
    codeBlock: TvmDisasmCodeBlock,
    stack: Stack = Stack(emptyList()),
    endingFromParentInst: TacOrdinaryInst? = null,
): ContGeneratedInfo {
    val tacInstructions = mutableListOf<AbstractTacInst>()

    for (inst in codeBlock.instList) {
        val curInstruction = processInstruction(ctx, stack, inst)
            ?: continue
        tacInstructions += curInstruction
    }

    if (endingFromParentInst != null) tacInstructions += endingFromParentInst  // insert 'return' at the end of CONT

    val methodArgs = stack.getCreatedArgs().map {
        TacVar(
            name = it.name,
            valueTypes = it.valueTypes,
        )
    }

    return ContGeneratedInfo(
        instructions = tacInstructions,
        arguments = methodArgs,
    )
}

private fun processInstruction(
    ctx: TacGenerationContext,
    stack: Stack,
    inst: TvmInst,
): AbstractTacInst? {
    throwErrorIfStackTypesNotSupported(inst)
    throwErrorIfBranchesNotTypeVar(inst)

    if (inst is TvmStackBasicInst || inst is TvmStackComplexInst) {
        return if (ctx.debug) {
            val stateBefore = stack.copyEntries()
            stack.execStackInstruction(inst)
            val stateAfter = stack.copyEntries()
            return TacDebugStackInst(
                mnemonic = inst.mnemonic,
                parameters = extractPrimitiveOperands(inst),
                stackBefore = stateBefore,
                stackAfter = stateAfter,
            )
        } else {
            stack.execStackInstruction(inst)
            null
        }
    }

    if (inst.mnemonic in CALLDICT_MNEMONICS) {
        val operands = extractPrimitiveOperands(inst).toMutableMap()

        var methodNumber = operands["n"] ?: error("Missing method number in CALLDICT")
        methodNumber = methodNumber as? Int
            ?: error("Expected method number to be Int in CALLDICT, but it is: ${methodNumber::class.simpleName}")
        methodNumber = methodNumber.toBigInteger()

        return processCallDict(ctx, stack, methodNumber, inst, operands)
    }

    return if (ctx.debug) {
        val stateBefore = stack.copyEntries()
        val innerInst = constructOrdinaryInst(ctx, stack, inst)
        val stateAfter = stack.copyEntries()
        return TacInstDebugWrapper(innerInst, stackBefore = stateBefore, stackAfter = stateAfter)
    } else {
        constructOrdinaryInst(ctx, stack, inst)
    }
}

private fun constructOrdinaryInst(
    ctx: TacGenerationContext,
    stack: Stack,
    inst: TvmInst,
): TacOrdinaryInst {
    val operands = extractPrimitiveOperands(inst).toMutableMap()

    val (allBranchesWithSave, endingInst) = checkAllBranchesWithSave(inst.branches, inst.mnemonic)
    val specInputs = inst.stackInputs ?: emptyList()
    val specOutputs = inst.stackOutputs ?: emptyList()

    var operandsContRefs: List<Int>? = null
    var stackContRef: Int? = null

    // what is that?
    if (inst is TvmContOperandInst) {
        val contRefs = processInstWithContsIsolated(ctx, inst)
        operandsContRefs = contRefs.first
        stackContRef = contRefs.second
    }

    var nonStackTacInst = stack.processNonStackInst(
        mnemonic = inst.mnemonic,
        stack = stack,
        inputSpec = specInputs,
        outputSpec = specOutputs,
        operands = operands,
        contRef = stackContRef
    )

    if (inst !is TvmConstDataInst) {  // i.e. operands don't contain CONT that meant to be pushed on stack
        nonStackTacInst.contIsolatedsRefs.addAll(operandsContRefs ?: emptyList())
    }

    if (nonStackTacInst.contIsolatedsRefs.isNotEmpty()) {

        val stackEntriesBefore = stack.copyEntries()

        val argsFromConts = nonStackTacInst.contIsolatedsRefs.mapNotNull { ref ->
            val cont = ctx.isolatedContinuations[ref]
            if (cont?.originalTvmCode == null) {
                println("WARNING: continuation with label $ref not found")
                null
            } else {
                cont.methodArgs
            }
        }

        val maxArgsList = argsFromConts.maxByOrNull { it.size }
        val totalArgsSize = maxArgsList!!.size

        val updatedStackBeforeConts = updateStack(stackEntriesBefore, maxArgsList, stack)
        val updatedStackEntriesBefore = stack.copyEntries()

        val processedContInfos = mutableListOf<ContProcessingInfo>()
        for (contRef in nonStackTacInst.contIsolatedsRefs) {
            val isolatedCont = ctx.isolatedContinuations[contRef]

            val continuationStack = updatedStackBeforeConts.copy()
            val (inlineInsts, inlineArgs) = generateTacCodeBlock(
                ctx,
                codeBlock = isolatedCont!!.originalTvmCode!!,
                stack = continuationStack,
                endingFromParentInst = endingInst,
            )

            val newRef = ctx.nextContinuationId()
            ctx.isolatedContinuations[newRef] = TacInlineMethod(
                instructions = inlineInsts,
                methodArgs = inlineArgs,
                originalTvmCode = isolatedCont.originalTvmCode,
                endingAssignmentStr = "",
            )

            nonStackTacInst.contStackPassedRefs.add(newRef)

            processedContInfos += ContProcessingInfo(
                contStackPassedRef = newRef,
                stackEntriesBefore = updatedStackEntriesBefore,
                stackEntriesAfter = continuationStack.copyEntries(),
                contArgsNum = totalArgsSize
            )
        }

        var instPrefix = ""

        when (processedContInfos.size) {
            1 -> {
                instPrefix = processStackEffectOneCont(
                    ctx,
                    processedContInfos,
                    allBranchesWithSave,
                    inst,
                    instPrefix,
                    stack,
                    nonStackTacInst
                )
            }

            2 -> {
                instPrefix =
                    processStackEffectTwoConts(ctx, processedContInfos, inst, nonStackTacInst, stack, instPrefix)
            }

            else -> error("Unexpected number of continuations: ${processedContInfos.size}")
        }
        nonStackTacInst = nonStackTacInst.copy(instPrefix = instPrefix)
    }

    return if (!allBranchesWithSave) {
        nonStackTacInst
    } else {
        nonStackTacInst.copy(saveC0 = true)
    }
}

fun haveSameTypes(stackElems: List<TacVar>, args: List<TacVar>): Boolean {
    return stackElems.zip(args).all { (b, a) ->
        a.valueTypes.isEmpty() || b.valueTypes.isEmpty() || a.valueTypes.contains("Null") || b.valueTypes.contains("Null") || b.valueTypes.any { it in a.valueTypes }
    }
}

fun generateTacContractCode(contract: TvmContractCode): TacContractCode<AbstractTacInst> {
    val ctx = TacGenerationContext(contract, debug = true)

    val (mainInstructions, mainArgs) = generateTacCodeBlock(ctx, codeBlock = contract.mainMethod)
    val main = TacMainMethod(
        instructions = mainInstructions,
        methodArgs = mainArgs,
    )

    val methods = contract.methods.mapValues { (id, method) ->
        val methodStack = Stack(emptyList())
        val (insts, methodArgs) = generateTacCodeBlock(ctx, codeBlock = method, stack = methodStack)

        TacMethod(
            methodId = id,
            instructions = insts,
            methodArgs = methodArgs,
            returnValues = methodStack.copyEntries(),
        )
    }

    return TacContractCode(
        mainMethod = main,
        methods = methods,
        isolatedContinuations = ctx.isolatedContinuations,
    )
}
