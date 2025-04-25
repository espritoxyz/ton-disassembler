package org.ton.tac

import org.ton.bytecode.TvmConstDataInst
import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmDisasmCodeBlock
import org.ton.bytecode.TvmInlineBlock
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.TvmStackBasicInst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.extractPrimitiveOperands
import kotlin.reflect.full.memberProperties

fun generateTacCodeBlock(codeBlock: TvmDisasmCodeBlock): Pair<MutableList<TacInst>, List<TacVar>> {
    val stack = Stack(emptyList())
    val tacInstructions = mutableListOf<TacInst>()

    for (inst in codeBlock.instList) {
        val operands = extractPrimitiveOperands(inst).toMutableMap()

        if (inst !is TvmStackBasicInst && inst !is TvmStackComplexInst) {
            if (inst.stackInputs == null || inst.stackOutputs == null) {
                error("Instruction: ${inst.mnemonic} is not supported, since stackInputs/Outputs are unconstrained")
            }

            val unsupportedInput = inst.stackInputs!!.any { it.type != "simple" && it.type != "const" }
            val unsupportedOutput = inst.stackOutputs!!.any { it.type != "simple" && it.type != "const" }

            if (unsupportedInput || unsupportedOutput) {
                error("Unsupported stack value type in instruction: ${inst.mnemonic}")
            }
        }

        val specInputs = inst.stackInputs
            ?.filter { it.type == "simple" || it.type == "const" }
            ?: emptyList()

        val specOutputs = inst.stackOutputs
            ?.filter { it.type == "simple" || it.type == "const" }
            ?: emptyList()

        if (inst is TvmStackBasicInst || inst is TvmStackComplexInst) {
            val stackTacInst = stack.execStackInstruction(inst)
            tacInstructions += stackTacInst
            continue
        }

        var continuationRefs: List<ContinuationRef>? = null
        var stackContRef: ContinuationRef? = null

        if (inst is TvmContOperandInst) {
            val continuationsList = inst::class
                .memberProperties
                .filter { it.returnType.classifier == TvmInstList::class }

            continuationRefs = continuationsList.map { cont ->
                val ref = ContinuationRef.next()
                val contList = (cont.getter.call(inst) as? TvmInstList)?.list ?: emptyList()
                val contBlock = TvmInlineBlock(contList.toMutableList())
                val (inlineInsts, inlineArgs) = generateTacCodeBlock(contBlock)

                inlineMethodPool[ref.label] = TacInlineMethod(
                    instructions = inlineInsts,
                    methodArgs = inlineArgs
                )

                ref
            }

            // If instruction is TvmConstDataInst, pass the first (and only) ref to the stack
            stackContRef =
                if (inst is TvmConstDataInst && continuationRefs.isNotEmpty()) continuationRefs.first() else null
        }
        val nonStackTacInst = stack.processNonStackInst(
            mnemonic = inst.mnemonic,
            stack = stack,
            inputSpec = specInputs,
            outputSpec = specOutputs,
            operands = operands,
            contRef = stackContRef
        )

        tacInstructions += if (inst is TvmConstDataInst) {
            nonStackTacInst // don't attach refs, they are linked to stack variables
        } else {
            nonStackTacInst.copy(continuationsRefs = continuationRefs)
        }


    }

    val methodArgs = stack.getCreatedArgs().map {
        TacVar(
            name = it.name,
            valueTypes = it.valueTypes,
        )
    }

    return tacInstructions to methodArgs
}

val inlineMethodPool = mutableMapOf<String, TacInlineMethod>()

fun generateTacContractCode(contract: TvmContractCode): TacContractCode {

    val (mainInstructions, mainArgs) = generateTacCodeBlock(contract.mainMethod)
    val main = TacMainMethod(
        instructions = mainInstructions,
        methodArgs = mainArgs
    )

    val methods = contract.methods.mapValues { (id, method) ->
        val (insts, methodArgs) = generateTacCodeBlock(method)
        TacMethod(
            methodId = id,
            instructions = insts,
            methodArgs = methodArgs
        )
    }

    return TacContractCode(
        mainMethod = main,
        methods = methods,
        inlineMethods = inlineMethodPool
    )
}