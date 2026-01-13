package org.ton.tac

import org.ton.bytecode.MethodId
import org.ton.bytecode.TvmConstDataInst
import org.ton.bytecode.TvmInlineBlock
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.TvmRealInst
import kotlin.reflect.full.memberProperties

val CALLDICT_MNEMONICS = setOf("CALLDICT", "CALLDICT_LONG")

data class ContProcessingInfo(
    val contStackPassedRef: Int,
    val stackEntriesBefore: List<TacStackValue>,
    val stackEntriesAfter: List<TacStackValue>,
    val contArgsNum: Int,
) {
    val stackTakenSize: Int get() = contArgsNum
    val stackPushedSize: Int get() = stackEntriesAfter.size - (stackEntriesBefore.size - contArgsNum)
}

internal data class OperandContinuationInfo(
    val operandContinuationIds: Map<String, Int>,
    val resultContinuationId: Int?, // for PUSHCONT
)

internal fun <Inst : AbstractTacInst> extractOperandContinuations(
    ctx: TacGenerationContext<Inst>,
    inst: TvmInst,
): OperandContinuationInfo {
    val continuationsList =
        inst::class
            .memberProperties
            .filter { it.returnType.classifier == TvmInstList::class }

    val operandsContinuations =
        continuationsList.associate { cont ->
            val contId = ctx.nextContinuationId()
            val contList = (cont.getter.call(inst) as? TvmInstList)?.list ?: emptyList()
            val contBlock = TvmInlineBlock(contList.toMutableList())
            val stack = Stack(emptyList())
            val (inlineInsts, inlineArgs) = generateTacCodeBlock(ctx, codeBlock = contBlock, stack)

            ctx.isolatedContinuations[contId] =
                TacContinuationInfo(
                    instructions = inlineInsts,
                    methodArgs = inlineArgs,
                    numberOfReturnedValues = stack.size,
                    originalTvmCode = contBlock,
                )

            cont.name to contId
        }

    val stackContRef =
        if (inst is TvmConstDataInst && operandsContinuations.isNotEmpty()) {
            // This is PUSHCONT or something similar
            operandsContinuations.values.single()
        } else {
            null
        }

    return OperandContinuationInfo(operandsContinuations, stackContRef)
}

internal fun <Inst : AbstractTacInst> processCallDict(
    ctx: TacGenerationContext<Inst>,
    stack: Stack,
    methodNumber: MethodId,
    inst: TvmInst,
    operands: Map<String, Any?>,
): TacOrdinaryInst<Inst> {
    val stackEntriesBefore = stack.copyEntries()
    val method =
        ctx.contract.methods[methodNumber]
            ?: error("Missing method with number $methodNumber")

    if (ctx.calledMethodsSet.contains(methodNumber)) {
        error("Recursive CALLDICT detected for method $methodNumber")
    }
    ctx.calledMethodsSet.add(methodNumber)

    val (_, methodArgs) = generateTacCodeBlock(ctx, codeBlock = method)

    val argsSize = methodArgs.size

    val methodStack = updateStack(stackEntriesBefore, methodArgs, stack)
    val updatedStackEntriesBefore = methodStack.copyEntries()

    val newRef = ctx.nextContinuationId()

    val processedMethodInfo =
        ContProcessingInfo(
            contStackPassedRef = newRef,
            stackEntriesBefore = updatedStackEntriesBefore,
            stackEntriesAfter = methodStack.copyEntries(),
            contArgsNum = argsSize,
        )

    val globalStackTakenSize = processedMethodInfo.stackTakenSize
    val globalStackPushedSize = processedMethodInfo.stackPushedSize

    val newAddedElems = processedMethodInfo.stackEntriesAfter.takeLast(globalStackPushedSize)

    repeat(globalStackTakenSize) {
        stack.pop(0)
    }

    newAddedElems.forEach {
        stack.push(it)
    }

    val nonStackTacInst =
        TacOrdinaryInst<Inst>(
            mnemonic = inst.mnemonic,
            inputs = stackEntriesBefore.takeLast(argsSize).reversed(),
            outputs = listOf(),
            operands = operands,
            blocks = emptyList(),
        )

    ctx.calledMethodsSet.remove(methodNumber)
    return nonStackTacInst
}

internal fun throwErrorIfBranchesNotTypeVar(inst: TvmRealInst) {
    if (inst.branches.isNotEmpty()) {
        val allBranchesTypeVar =
            inst.branches.all { branch ->
                branch.type == "variable" || branch.type == "register"
            }
        if (!allBranchesTypeVar && inst.mnemonic !in CALLDICT_MNEMONICS) {
            TODO(
                "Instruction ${inst.mnemonic}: branch must be type 'variable' or 'register', got: ${inst.branches.map {
                    it.type
                }}",
            )
        }
    }
}
