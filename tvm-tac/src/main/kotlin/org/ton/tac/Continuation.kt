package org.ton.tac

import org.ton.bytecode.TvmConstDataInst
import org.ton.bytecode.TvmControlFlowContinuation
import org.ton.bytecode.TvmInlineBlock
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmInstList
import java.math.BigInteger
import kotlin.reflect.full.memberProperties

val CALLDICT_MNEMONICS = setOf("CALLDICT", "CALLDICT_LONG")

data class ContProcessingInfo(
    val contStackPassedRef: Int,
    val stackEntriesBefore: List<TacVar>,
    val stackEntriesAfter: List<TacVar>,
    val contArgsNum: Int
) {
    val stackTakenSize: Int get() = contArgsNum
    val stackPushedSize: Int get() = stackEntriesAfter.size - (stackEntriesBefore.size - contArgsNum)
    val stackDelta: Int get() = stackPushedSize - stackTakenSize
}

fun checkAllBranchesWithSave(
    branches: List<TvmControlFlowContinuation>,
    mnemonic: String
): Pair<Boolean, TacOrdinaryInst?> {
    var allBranchesWithSave = true
    var endingInst: TacOrdinaryInst? = null
    if (branches.isNotEmpty()) {
        allBranchesWithSave = branches.all { it.save != null }
        if (!allBranchesWithSave) {
            endingInst = TacOrdinaryInst(
                mnemonic = "return",
                inputs = emptyList(),
                outputs = emptyList(),
                operands = mutableMapOf(),
                contIsolatedsRefs = mutableListOf(),
                contStackPassedRefs = mutableListOf(),
                saveC0 = false,
                instPrefix = ""
            )
        } else {
            val allVariablesWithCcSaveOnly = branches.all { branch ->
                branch.save?.get("c0")?.type == "cc" &&
                            branch.save!!.size == 1
            }
            if (!allVariablesWithCcSaveOnly) {
                error("we can't process branches in $mnemonic")
            }
        }
    }

    return Pair(allBranchesWithSave, endingInst)
}

fun processInstWithContsIsolated(
    ctx: TacGenerationContext,
    inst: TvmInst,
): Pair<List<Int>?, Int?> {
    val continuationsList = inst::class
        .memberProperties
        .filter { it.returnType.classifier == TvmInstList::class }

    val operandsContinuations = continuationsList.map { cont ->
        val contId = ctx.nextContinuationId()
        val contList = (cont.getter.call(inst) as? TvmInstList)?.list ?: emptyList()
        val contBlock = TvmInlineBlock(contList.toMutableList())
        val (inlineInsts, inlineArgs) = generateTacCodeBlock(ctx, codeBlock = contBlock)

        ctx.isolatedContinuations[contId] = TacInlineMethod(
            instructions = inlineInsts,
            methodArgs = inlineArgs,
            originalTvmCode = contBlock,  // for later execution
            endingAssignmentStr = ""
        )

        contId
    }

    // If instruction is TvmConstDataInst, pass the first (and only) ref to the stack
    val stackContRef =
        if (inst is TvmConstDataInst && operandsContinuations.isNotEmpty()) operandsContinuations.first() else null

    return Pair(operandsContinuations, stackContRef)
}

internal fun processCallDict(
    ctx: TacGenerationContext,
    stack: Stack,
    methodNumber: BigInteger,
    inst: TvmInst,
    operands: MutableMap<String, Any?>
): TacOrdinaryInst {
    val stackEntriesBefore = stack.copyEntries()
    val method = ctx.contract.methods[methodNumber]
        ?: error("Missing method with number $methodNumber")

    if (ctx.calledMethodsSet.contains(methodNumber)) {
        error("Recursive CALLDICT detected for method $methodNumber")
    }
    ctx.calledMethodsSet.add(methodNumber)

    val (_, methodArgs) = generateTacCodeBlock(ctx, codeBlock = method)

    val argsSize = methodArgs.size
    val methodStack = updateStack(stackEntriesBefore, methodArgs, stack)
    val updatedStackEntriesBefore = methodStack.copyEntries()

    val (inlineInsts, inlineArgs) = generateTacCodeBlock(
        ctx,
        codeBlock = method,
        stack = methodStack,
    )

    val newRef = ctx.nextContinuationId()
    ctx.isolatedContinuations[newRef] = TacInlineMethod(
        instructions = inlineInsts,
        methodArgs = inlineArgs,
        originalTvmCode = method,
        endingAssignmentStr = "",
    )

    val processedMethodInfo = ContProcessingInfo(
        contStackPassedRef = newRef,
        stackEntriesBefore = updatedStackEntriesBefore,
        stackEntriesAfter = methodStack.copyEntries(),
        contArgsNum = argsSize
    )
    val globalStackTakenSize = processedMethodInfo.stackTakenSize
    val globalStackPushedSize = processedMethodInfo.stackPushedSize
    val newAddedElems = processedMethodInfo.stackEntriesAfter.takeLast(globalStackPushedSize)

    stack.dropLastInPlace(globalStackTakenSize)
    stack.addAll(newAddedElems)

    val nonStackTacInst = TacOrdinaryInst(
        mnemonic = inst.mnemonic,
        inputs = stackEntriesBefore.takeLast(argsSize).reversed(),
        outputs = listOf(),
        operands = operands,
        saveC0 = true,
    )
//    nonStackTacInst.debugInfo += "globalStackTakenSize: $globalStackTakenSize, globalStackPushedSize: $globalStackPushedSize" // delete after debug
    nonStackTacInst.instSuffix = "new stack elems [${newAddedElems.joinToString(", ") { it.name }}]"

    ctx.calledMethodsSet.remove(methodNumber)
    return nonStackTacInst
}

fun addEndingAssignmentToCont(
    ctx: TacGenerationContext,
    globalPushedValues: List<TacVar>,
    contInfo: ContProcessingInfo,
) {
    val endingAssignment = globalPushedValues
        .zip(contInfo.stackEntriesAfter.takeLast(globalPushedValues.size))
        .joinToString(", ") { (lhs, rhs) -> "${lhs.name} = ${rhs.name}" }

    val continuation = ctx.isolatedContinuations[contInfo.contStackPassedRef]
        ?: error("Continuation with id ${contInfo.contStackPassedRef} not found")

    ctx.isolatedContinuations[contInfo.contStackPassedRef] = continuation.copy(endingAssignmentStr = endingAssignment)
}

internal fun throwErrorIfBranchesNotTypeVar(inst: TvmInst) {
    if (inst.branches.isNotEmpty()) {
        val allBranchesTypeVar = inst.branches.all { branch ->
            branch.type == "variable"
        }
        if (!allBranchesTypeVar && inst.mnemonic !in CALLDICT_MNEMONICS) {
            error("in ${inst.mnemonic} branch isn't type variable")
        }
    }
}

internal fun processStackEffectOneCont(
    ctx: TacGenerationContext,
    processedContInfos: MutableList<ContProcessingInfo>,
    allBranchesWithSave: Boolean,
    inst: TvmInst,
    instPrefix: String,
    stack: Stack,
    tacOrdinaryInst: TacOrdinaryInst
): String {
    var newInstPrefix = instPrefix
    val contInfo = processedContInfos.first()
    val globalStackTakenSize = contInfo.stackTakenSize
    val globalStackPushedSize = contInfo.stackPushedSize
    val stackEntriesBeforeTaken = contInfo.stackEntriesBefore.takeLast(globalStackPushedSize)
    val newElemsCont = contInfo.stackEntriesAfter.takeLast(globalStackPushedSize)
    val globalPushedValues = newElemsCont.map { elem ->
        TacVar(
            name = ctx.nextContVarName(),
            valueTypes = elem.valueTypes.toList(),
            concreteContinuationRef = elem.concreteContinuationRef
        )
    }

    val instPrefixAssignment = globalPushedValues
        .zip(stackEntriesBeforeTaken)
        .joinToString(separator = ", ") { (lhs, rhs) -> "${lhs.name} = ${rhs.name}" }

    val instPrefixVars = globalPushedValues.joinToString(", ") { it.name }

    if (allBranchesWithSave) {  // we check stack effect for insts with 'save'
        val stackDelta = contInfo.stackDelta
        if (!inst.noBranch) {
            if (instPrefixVars.isNotEmpty()) {
                newInstPrefix = "lateinit $instPrefixVars"
            }

        } else {
            if (instPrefixAssignment.isNotEmpty()) {
                newInstPrefix = "mut $instPrefixAssignment"
            }
            if (stackDelta != 0) {  // this is how we check stack effect so far
                println("WARNING: non-null effect on stack in ${inst.mnemonic} in ${contInfo.contStackPassedRef}")
            }
        }

        stack.dropLastInPlace(globalStackTakenSize)
        stack.addAll(globalPushedValues)

        addEndingAssignmentToCont(ctx, globalPushedValues, contInfo)
    }

//    tacOrdinaryInst.debugInfo += "globalStackTakenSize: $globalStackTakenSize, globalStackPushedSize: $globalStackPushedSize"
    return newInstPrefix
}

internal fun processStackEffectTwoConts(
    ctx: TacGenerationContext,
    processedContInfos: MutableList<ContProcessingInfo>,
    inst: TvmInst,
    tacOrdinaryInst: TacOrdinaryInst,
    stack: Stack,
    instPrefix: String
): String {
    var newInstPrefix = instPrefix
    val (contInfo1, contInfo2) = processedContInfos
    val stackDelta1 = contInfo1.stackDelta
    val stackDelta2 = contInfo2.stackDelta

    val commonStackEffect = stackDelta2 - stackDelta1
    if (commonStackEffect != 0) {  // this is how we check stack effect so far
        println("WARNING: branches causes different stack effect in ${inst.mnemonic}\nbranches: ${processedContInfos[0].contStackPassedRef} and ${processedContInfos[1].contStackPassedRef}")
    }

    val globalStackTakenSize = maxOf(contInfo1.stackTakenSize, contInfo2.stackTakenSize)
    val globalStackPushedSize = maxOf(contInfo1.stackPushedSize, contInfo2.stackPushedSize)

    val newElemsCont1 = contInfo1.stackEntriesAfter.takeLast(globalStackPushedSize)
    val newElemsCont2 = contInfo2.stackEntriesAfter.takeLast(globalStackPushedSize)
    val newElemsSameType = haveSameTypes(newElemsCont1, newElemsCont2)
    if (!newElemsSameType) {
        println("WARNING: branches in instruction ${inst.mnemonic} with conts ${tacOrdinaryInst.contIsolatedsRefs} and ${tacOrdinaryInst.contStackPassedRefs}  have different pushed values: $newElemsCont1 and $newElemsCont2")
    }

    val globalPushedValues: List<TacVar> = newElemsCont1.zip(newElemsCont2).map { (a, b) ->
        val mergedTypes = when {
            a.valueTypes.isEmpty() -> b.valueTypes
            b.valueTypes.isEmpty() -> a.valueTypes
            else -> (a.valueTypes + b.valueTypes).toSet().toList()
        }

        if (a.concreteContinuationRef != b.concreteContinuationRef) {
            println("WARNING: different continuations in ${inst.mnemonic}: ${a.concreteContinuationRef} and ${b.concreteContinuationRef}")
        }

        TacVar(
            name = ctx.nextContVarName(),
            valueTypes = mergedTypes,
            concreteContinuationRef = a.concreteContinuationRef
        )
    }

    stack.dropLastInPlace(globalStackTakenSize)
    stack.addAll(globalPushedValues)

    addEndingAssignmentToCont(ctx, globalPushedValues, contInfo1)
    addEndingAssignmentToCont(ctx, globalPushedValues, contInfo2)

    val instPrefixVars = globalPushedValues.joinToString(", ") { it.name }
    if (instPrefixVars.isNotEmpty()) {
        newInstPrefix = "lateinit $instPrefixVars"
    }

//    tacOrdinaryInst.debugInfo += "globalStackTakenSize: $globalStackTakenSize, globalStackPushedSize: $globalStackPushedSize"
    return newInstPrefix
}