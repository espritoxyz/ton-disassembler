package org.ton.tac

import org.ton.bytecode.TvmCell
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.formatOperand

fun dumpTacContract(contract: TacContractCode, includeTvmCell: Boolean = false, debug: Boolean = false): String {
    val builder = StringBuilder()

    builder.appendLine("Main method:")
    builder.appendLine(dumpTacCodeBlock(contract.mainMethod, includeTvmCell, contract.inlineMethods, debug = debug))

    for ((methodId, method) in contract.methods) {
        builder.appendLine("\nMethod ID: $methodId")
        builder.appendLine(dumpTacCodeBlock(method, includeTvmCell, contract.inlineMethods, debug = debug))
    }

    if (debug) {
        if (contract.inlineMethods.isNotEmpty()) {
            builder.appendLine("\nInline Methods:")
            for ((contIndex, contMethod) in contract.inlineMethods) {
                builder.appendLine("\nContinuation_index: $contIndex")
                builder.appendLine(dumpTacCodeBlock(contMethod, includeTvmCell, contract.inlineMethods, debug = debug))
            }
        }
    }

    return builder.toString()
}

fun dumpOperands(operands: Map<String, Any?>, includeTvmCell: Boolean = false): String {
    val operandStrings =
        operands
            .filterValues { it !is TvmInstList }
            .map { (key, value) ->
                "$key=${if (includeTvmCell) {
                    formatOperand(value)
                } else if (value !is TvmCell) {
                    "$value"
                } else {
                    "[Cell]"
                }}"
            }
    val operandsStr = operandStrings.joinToString(", ")
    return operandsStr
}

fun dumpTacCodeBlock(
    block: TacCodeBlock,
    includeTvmCell: Boolean = false,
    inlineMethods: Map<String, TacInlineMethod>,
    indent: String = "  ",
    debug: Boolean = false,
    isRecursive: Boolean = false,
    endingContAssignment: String = ""
) : String {
    val builder = StringBuilder()

    val argsStr = block.methodArgs.joinToString(", ") { arg ->
        if (arg.valueTypes.isNotEmpty()) "${arg.name}: ${arg.valueTypes.joinToString(" | ")}" else arg.name
    }

    if (debug || !isRecursive) {
        builder.appendLine(indent + "function($argsStr) {")
    } else {
        builder.appendLine("{")
    }


    for (inst in block.instructions) {
        if (inst is StackTacInst && !debug) continue

        val base = buildString {
            when (inst) {
                is StackTacInst -> {
                    appendLine(indent + "${inst.mnemonic} ${dumpOperands(inst.operands, includeTvmCell)}")
                    append(indent + "  // ${inst.stackState}")
                }

                is NonStackTacInst -> {
                    val instPrefix = buildString {
                            append(indent)
                            append(inst.instPrefix)
                    }

                    val instLine = buildString {
                        append(indent)
                        if (inst.outputs.isNotEmpty()) {
                            append(inst.outputs.joinToString(", ") { output ->
                                val continuationSuffix = if (debug && "Continuation" in output.valueTypes && output.contRef != null) {
                                    " -> ${output.contRef.label}"
                                } else ""
                                output.name + continuationSuffix
                            } + " = ")
                        }
                        append("${inst.mnemonic}(")
                        append(
                            inst.inputs
                                .filter { "Continuation" !in it.valueTypes}
                                .joinToString(", ") { it.name }
                        )

                        if (debug && inst.contIsolatedsRefs.isNotEmpty()) {
                            append(", isolatedRefs:" + inst.contIsolatedsRefs.joinToString(", ") { " -> ${it.label}, " })
                        }
                        if (debug && inst.contStackPassedRefs.isNotEmpty()) {
                            append("stackPassedRefs:" + inst.contStackPassedRefs.joinToString(", ") { " -> ${it.label}, " })
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

                        if (debug && inst.contIsolatedsRefs.isNotEmpty()) {
                            appendLine(indent + "  // ${inst.debugInfo}")
                        }

                        if (inst.contStackPassedRefs.isNotEmpty()) {
                            val contRefs = inst.contStackPassedRefs.map { it.label }
                            contRefs.forEach { ref ->
                                val inlineMethod = inlineMethods[ref]
                                if (inlineMethod != null) {
                                    val endingContAssignmentStr = inlineMethod.endingAssignmentStr
                                    append(dumpTacCodeBlock(block = inlineMethod, includeTvmCell = includeTvmCell ,inlineMethods = inlineMethods, indent = "$indent  ", debug = debug, isRecursive = true, endingContAssignment = endingContAssignmentStr))
                                }
                            }
                        }
                    }

//                    if (prefix.isNotEmpty()) append(indent + prefix)
                    if (inst.instPrefix.isNotEmpty()) appendLine(instPrefix.trimEnd())
                    append(instLine.trimEnd())

                }
            }
        }

        builder.appendLine(base)

        if (inst is NonStackTacInst && debug && inst.debugInfo != null && inst.contStackPassedRefs.isEmpty()) {
            builder.appendLine(indent + "  // ${inst.debugInfo}")
        }

        if (inst is NonStackTacInst && !inst.warningInfo.isNullOrBlank()) {
            builder.appendLine(indent + "  // WARNING: ${inst.warningInfo}")
        }
    }

    if (endingContAssignment.isNotEmpty()) builder.appendLine(indent + endingContAssignment)

    val resultStack = (block as? TacMethod)?.resultStack
    if (resultStack != null) {
        builder.appendLine(indent + "return [${resultStack.joinToString(", ") { it.name }}]")
    }

    builder.append(indent.removeSuffix("  ") + "}")
    return builder.toString()
}
