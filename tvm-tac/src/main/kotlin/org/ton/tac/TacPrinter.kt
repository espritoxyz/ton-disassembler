package org.ton.tac

import org.ton.bytecode.TvmCell
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.formatOperand

fun dumpTacContract(contract: TacContractCode<AbstractTacInst>, includeTvmCell: Boolean = false): String {
    val builder = StringBuilder()

    builder.appendLine("Main method:")
    builder.appendLine(dumpTacCodeBlock(contract.mainMethod, includeTvmCell, contract.isolatedContinuations))

    for ((methodId, method) in contract.methods) {
        builder.appendLine("\nMethod ID: $methodId")
        builder.appendLine(dumpTacCodeBlock(method, includeTvmCell, contract.isolatedContinuations))
    }

    if (contract.isolatedContinuations.isNotEmpty()) {
        builder.appendLine("\nInline Methods:")
        for ((contIndex, contMethod) in contract.isolatedContinuations) {
            builder.appendLine("\nContinuation_index: $contIndex")
            builder.appendLine(dumpTacCodeBlock(contMethod, includeTvmCell, contract.isolatedContinuations))
        }
    }

    return builder.toString()
}

fun dumpOperands(operands: Map<String, Any?>, includeTvmCell: Boolean = false): String {
    val operandStrings =
        operands
            .filterValues { it !is TvmInstList }
            .map { (key, value) ->
                "$key=${
                    if (includeTvmCell) {
                        formatOperand(value)
                    } else if (value !is TvmCell) {
                        "$value"
                    } else {
                        "[Cell]"
                    }
                }"
            }
    val operandsStr = operandStrings.joinToString(", ")
    return operandsStr
}

private fun printStackState(stack: List<TacVar>) =
    stack.joinToString(prefix = "stack: [", postfix = "]") { it.name }

fun dumpTacCodeBlock(
    block: TacCodeBlock<AbstractTacInst>,
    includeTvmCell: Boolean = false,
    inlineMethods: Map<Int, TacInlineMethod<AbstractTacInst>>,
    indent: String = "  ",
    isRoot: Boolean = true,
    endingContAssignment: String = ""
): String {
    val builder = StringBuilder()

    val argsStr = block.methodArgs.joinToString(", ") { arg ->
        if (arg.valueTypes.isNotEmpty()) "${arg.name}: ${arg.valueTypes.joinToString(" | ")}" else arg.name
    }

    if (isRoot) {
        builder.appendLine("function($argsStr) {")
    } else {
        builder.appendLine("{")
    }


    for (inst in block.instructions) {
        val base = buildString {
            when (inst) {
                is TacDebugStackInst -> {
                    appendLine(indent + "${inst.mnemonic} ${dumpOperands(inst.parameters, includeTvmCell)}")
                    append(indent + "  // ${printStackState(inst.stackAfter)}")
                }

                is TacInstDebugWrapper -> {
                    TODO()
                }

                is TacReturnInst -> {
                    TODO()
                }

                is TacOrdinaryInst -> {
                    val instPrefix = buildString {
                        append(indent)
                        append(inst.instPrefix)
                    }

                    val instLine = buildString {
                        append(indent)
                        if (inst.outputs.isNotEmpty()) {
                            append(inst.outputs.joinToString(", ") { output ->
                                val continuationSuffix =
                                    if ("Continuation" in output.valueTypes && output.contRef != null) {
                                        " -> cont_${output.contRef}"
                                    } else ""
                                output.name + continuationSuffix
                            } + " = ")
                        }
                        append("${inst.mnemonic}(")
                        append(
                            inst.inputs
                                .filter { "Continuation" !in it.valueTypes }
                                .joinToString(", ") { it.name }
                        )

                        if (inst.contIsolatedsRefs.isNotEmpty()) {
                            append(", isolatedRefs:" + inst.contIsolatedsRefs.joinToString(", ") { " -> cont_$it, " })
                        }
                        if (inst.contStackPassedRefs.isNotEmpty()) {
                            append("stackPassedRefs:" + inst.contStackPassedRefs.joinToString(", ") { " -> cont_$it., " })
                        }

                        if (inst.operands.isNotEmpty()) {
                            val operandsStr = dumpOperands(inst.operands, includeTvmCell)
                            if (inst.inputs.isNotEmpty() && operandsStr.isNotEmpty()) append(", ")
                            append(operandsStr)
                        }
                        append(")")

                        if (inst.instSuffix.isNotEmpty()) {
                            appendLine()
                            append(indent + inst.instSuffix)
                        }

                        if (inst.contIsolatedsRefs.isNotEmpty()) {
                            appendLine(indent + "  // ${inst.debugInfo}")
                        }

                        if (inst.contStackPassedRefs.isNotEmpty()) {
                            val contRefs = inst.contStackPassedRefs
                            contRefs.forEach { ref ->
                                val inlineMethod = inlineMethods[ref]
                                if (inlineMethod != null) {
                                    val endingContAssignmentStr = inlineMethod.endingAssignmentStr
                                    append(
                                        dumpTacCodeBlock(
                                            block = inlineMethod,
                                            includeTvmCell = includeTvmCell,
                                            inlineMethods = inlineMethods,
                                            indent = "$indent  ",
                                            isRoot = false,
                                            endingContAssignment = endingContAssignmentStr
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (inst.instPrefix.isNotEmpty()) appendLine(instPrefix.trimEnd())
                    append(instLine.trimEnd())

                }
            }
        }

        builder.appendLine(base)

        if (inst is TacOrdinaryInst && inst.debugInfo != null && inst.contStackPassedRefs.isEmpty()) {
            builder.appendLine(indent + "  // ${inst.debugInfo}")
        }

        if (inst is TacOrdinaryInst && !inst.warningInfo.isNullOrBlank()) {
            builder.appendLine(indent + "  // WARNING: ${inst.warningInfo}")
        }
    }

    if (endingContAssignment.isNotEmpty()) builder.appendLine(indent + endingContAssignment)

    val resultStack = (block as? TacMethod)?.returnValues
    if (resultStack != null) {
        builder.appendLine(indent + "return [${resultStack.joinToString(", ") { it.name }}]")
    }

    builder.append(indent.removeSuffix("  ") + "}")
    return builder.toString()
}
