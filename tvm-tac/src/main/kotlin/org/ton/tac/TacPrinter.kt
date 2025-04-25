package org.ton.tac

import org.ton.bytecode.TvmCell
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.formatOperand

fun dumpTacContract(contract: TacContractCode, includeTvmCell: Boolean = false, debug: Boolean = false): String {
    val builder = StringBuilder()

    builder.appendLine("Main method:")
    builder.appendLine(dumpTacCodeBlock(contract.mainMethod, includeTvmCell, debug))

    for ((methodId, method) in contract.methods) {
        builder.appendLine("\nMethod ID: $methodId")
        builder.appendLine(dumpTacCodeBlock(method, includeTvmCell, debug))
    }

    if (contract.inlineMethods.isNotEmpty()) {
        builder.appendLine("\nInline Methods:")
        for ((contIndex, contMethod) in contract.inlineMethods) {
            builder.appendLine("\nContinuation_index: $contIndex")
            builder.appendLine(dumpTacCodeBlock(contMethod, includeTvmCell, debug))
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

fun dumpTacCodeBlock(block: TacCodeBlock, includeTvmCell: Boolean = false, debug: Boolean = false): String {
    val builder = StringBuilder()

    val argsStr = block.methodArgs.joinToString(", ") { arg ->
        if (arg.valueTypes.isNotEmpty()) "${arg.name}: ${arg.valueTypes.joinToString(" | ")}" else arg.name
    }

    builder.appendLine("function($argsStr) {")

    for (inst in block.instructions) {
        if (inst is StackTacInst && !debug) continue

        val base = buildString {
            when (inst) {
                is StackTacInst -> {
                    append("${inst.mnemonic} ${dumpOperands(inst.operands, includeTvmCell)}")
                    append("  // ${inst.stackState}")
                }

                is NonStackTacInst -> {
                    if (inst.label != null) appendLine("${inst.label}:")
                    if (inst.outputs.isNotEmpty()) {
                        append(inst.outputs.joinToString(", ") { output ->
                            val continuationSuffix = if ("Continuation" in output.valueTypes && output.contRef != null) {
                                " -> ${output.contRef.label}"
                            } else ""
                            output.name + continuationSuffix
                        } + " = ")
                    }
                    append("${inst.mnemonic}(")
                    append(inst.inputs.joinToString(", ") { it.name })

                    if (inst.operands.isNotEmpty()) {
                        if (inst.inputs.isNotEmpty()) append(", ")
                        append(dumpOperands(inst.operands, includeTvmCell))
                        if (inst.continuationsRefs != null)
                            append(" -> ${inst.continuationsRefs.joinToString(", ") { it.label }}")
                    }

                    append(")")

                    if (inst.goto != null) append(" goto ${inst.goto}")
                }
            }
        }

        builder.appendLine("  $base")

        if (inst is NonStackTacInst && debug && inst.debugInfo != null) {
            builder.appendLine("  // ${inst.debugInfo}")
        }

        if (inst is NonStackTacInst && !inst.warningInfo.isNullOrBlank()) {
            builder.appendLine("  // WARNING: ${inst.warningInfo}")
        }
    }

    builder.append("}")
    return builder.toString()
}
