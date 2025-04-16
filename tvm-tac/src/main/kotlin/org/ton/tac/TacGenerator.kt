package org.ton.tac

import org.ton.bytecode.TvmConstDataInst
import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmDisasmCodeBlock
import org.ton.bytecode.TvmStackBasicInst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.extractPrimitiveOperands

data class ContGeneratedInfo(
    val contInstructions: MutableList<TacInst>,
    val contArgs: List<TacVar>,
    val contStackAfter: Stack
)

object TacVarFactory {
    private var contVarCounter = 0

    fun nextContVarName(): String {
        return "var_${contVarCounter++}"
    }
}

fun generateTacCodeBlock(
    codeBlock: TvmDisasmCodeBlock,
    stack: Stack = Stack(emptyList()),
    endingFromParentInst: NonStackTacInst? = null,
    contract: TvmContractCode,
    calledMethodsSet: MutableSet<MethodId> = mutableSetOf(),
): ContGeneratedInfo {
    val tacInstructions = mutableListOf<TacInst>()

    for (inst in codeBlock.instList) {
        val operands = extractPrimitiveOperands(inst).toMutableMap()

        throwErrorIfStackTypesNotSupported(inst)
        throwErrorIfBranchesNotTypeVar(inst)

        val (allBranchesWithSave, endingInst) = checkAllBranchesWithSave(inst.branches, inst.mnemonic)
        val specInputs = inst.stackInputs ?: emptyList()
        val specOutputs = inst.stackOutputs ?: emptyList()

        if (inst is TvmStackBasicInst || inst is TvmStackComplexInst) {
            val stackTacInst = stack.execStackInstruction(inst)
            tacInstructions += stackTacInst
            continue
        }

        var operandsContRefs: List<ContinuationRef>? = null
        var stackContRef: ContinuationRef? = null

        if (inst is TvmContOperandInst) {
            val contRefs = processInstWithContsIsolated(inst, contract)
            operandsContRefs = contRefs.first
            stackContRef = contRefs.second
        }

        if (inst.mnemonic in CALLDICT_MNEMONICS) {
            var methodNumber = operands["n"] ?: error("Missing method number in CALLDICT")
            methodNumber = methodNumber as? Int
                ?: error("Expected method number to be Int in CALLDICT, but it is: ${methodNumber::class.simpleName}")
            methodNumber = methodNumber.toBigInteger()
            val nonStackTacInst = processCALLDICT(stack, contract, methodNumber, calledMethodsSet, inst, operands)

            tacInstructions += nonStackTacInst
            continue
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

            var stackEntriesBefore = stack.copyEntries()

            val argsFromConts = nonStackTacInst.contIsolatedsRefs.mapNotNull { ref ->
                val cont = inlineMethodPool[ref.label]
                if (cont?.originalTvmCode == null) {
                    println("WARNING: continuation with label ${ref.label} not found")
                    null
                } else {
                    cont.methodArgs
                }
            }

            val maxArgsList = argsFromConts.maxByOrNull { it.size }
            val totalArgsSize = maxArgsList!!.size

            val updatedStackBeforeConts = updateStack(stackEntriesBefore, maxArgsList,stack)
            val updatedStackEntriesBefore = stack.copyEntries()

            val processedContInfos = mutableListOf<ContProcessingInfo>()
            for (contRef in nonStackTacInst.contIsolatedsRefs) {
                val isolatedCont = inlineMethodPool[contRef.label]

                val (inlineInsts, inlineArgs, stackAfterCont) = generateTacCodeBlock(
                    codeBlock = isolatedCont!!.originalTvmCode!!,
                    stack = updatedStackBeforeConts.copy(),
                    endingFromParentInst = endingInst,
                    contract = contract
                )

                val newRef = ContinuationRef.next()
                inlineMethodPool[newRef.label] = TacInlineMethod(
                    instructions = inlineInsts,
                    methodArgs = inlineArgs,
                    originalTvmCode = isolatedCont.originalTvmCode,
                    endingAssignmentStr = "",
                )

                nonStackTacInst.contStackPassedRefs.add(newRef)

                processedContInfos += ContProcessingInfo(
                    contStackPassedRef = newRef,
                    stackEntriesBefore = updatedStackEntriesBefore,
                    stackEntriesAfter = stackAfterCont.copyEntries(),
                    contArgsNum = totalArgsSize
                )
            }

            var instPrefix = ""

            when (processedContInfos.size) {
                1 -> {
                    instPrefix = processStackEffectOneCont(processedContInfos, allBranchesWithSave, inst, instPrefix, stack, nonStackTacInst)
                }
                2 -> {
                    instPrefix = processStackEffectTwoConts(processedContInfos, inst, nonStackTacInst, stack, instPrefix)
                }
                else -> error("Unexpected number of continuations: ${processedContInfos.size}")
            }
            nonStackTacInst = nonStackTacInst.copy(instPrefix = instPrefix)
        }

        tacInstructions += if (!allBranchesWithSave) {
            nonStackTacInst
        } else {
            nonStackTacInst.copy(saveC0 = true)
        }
    }

    if (endingFromParentInst != null) tacInstructions += endingFromParentInst  // insert 'return' at the end of CONT

    val methodArgs = stack.getCreatedArgs().map {
        TacVar(
            name = it.name,
            valueTypes = it.valueTypes,
        )
    }

    return ContGeneratedInfo(
        contInstructions = tacInstructions,
        contArgs = methodArgs,
        contStackAfter = stack
    )
}

fun haveSameTypes(stackElems: List<TacVar>, args: List<TacVar>): Boolean {
    return stackElems.zip(args).all { (b, a) ->
        a.valueTypes.isEmpty() || b.valueTypes.isEmpty() || a.valueTypes.contains("Null") || b.valueTypes.contains("Null") ||  b.valueTypes.any { it in a.valueTypes }
    }
}

val inlineMethodPool = mutableMapOf<String, TacInlineMethod>()

fun generateTacContractCode(contract: TvmContractCode): TacContractCode {

    val (mainInstructions, mainArgs, _) = generateTacCodeBlock(
        codeBlock = contract.mainMethod,
        contract = contract
    )
    val main = TacMainMethod(
        instructions = mainInstructions,
        methodArgs = mainArgs
    )

    val methods = contract.methods.mapValues { (id, method) ->
        val (insts, methodArgs, methodAfterStack) = generateTacCodeBlock(
            codeBlock = method,
            contract = contract
        )

        TacMethod(
            methodId = id,
            instructions = insts,
            methodArgs = methodArgs,
            originalTvmCode = method,
            resultStack = methodAfterStack.copyEntries(),
        )
    }

    return TacContractCode(
        mainMethod = main,
        methods = methods,
        inlineMethods = inlineMethodPool
    )
}