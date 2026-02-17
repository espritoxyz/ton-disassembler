package org.ton.tac

import org.ton.bytecode.TvmCell
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.formatOperand

fun dumpTacContract(
    contract: TacContractCode<AbstractTacInst>,
    includeTvmCell: Boolean = false,
): String {
    val builder = StringBuilder()

    builder.appendLine("Main method:")
    builder.appendLine(dumpTacCodeBlock(contract.mainMethod, includeTvmCell))

    for ((methodId, method) in contract.methods) {
        builder.appendLine("\nMethod ID: $methodId")
        builder.appendLine(dumpTacCodeBlock(method, includeTvmCell))
    }

    return builder.toString()
}

fun dumpOperands(
    operands: Map<String, Any?>,
    includeTvmCell: Boolean,
): String {
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

fun dumpTacCodeBlock(
    code: TacCodeBlock<AbstractTacInst>,
    includeTvmCell: Boolean,
): String =
    buildString {
        val args =
            code.methodArgs.joinToString {
                if (it.valueTypes.size == 1) {
                    "${it.name}: ${it.valueTypes.single()}"
                } else {
                    it.name
                }
            }
        appendLine("function ($args) {")
        dumpTacCodeBlock(code.instructions, includeTvmCell, indent = " ".repeat(INDENT))
        appendLine("}")
    }

private fun StringBuilder.dumpTacCodeBlock(
    code: List<AbstractTacInst>,
    includeTvmCell: Boolean,
    indent: String,
) {
    code.forEach {
        dumpInstruction(it, includeTvmCell, indent)
    }
}

private fun dumpStackState(stack: List<TacStackValue>) =
    stack.joinToString(prefix = "stack: [", postfix = "]") {
        it.name
    }

private fun StringBuilder.dumpInstruction(
    inst: AbstractTacInst,
    includeTvmCell: Boolean,
    indent: String,
) {
    when (inst) {
        is TacDebugInst -> dumpInstruction(inst, includeTvmCell, indent)
        is TacInst -> dumpInstruction(inst, includeTvmCell, indent)
    }
}

private fun StringBuilder.dumpInstruction(
    inst: TacPopCtrInst,
    indent: String,
) {
    append(indent)
    append("${inst.value.name} = POPCTR c${inst.registerIndex}")
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacPushCtrInst,
    indent: String,
) {
    append(indent)
    append("${inst.value.name} = PUSHCTR c${inst.registerIndex}")
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacDebugInst,
    includeTvmCell: Boolean,
    indent: String,
) {
    when (inst) {
        is TacInstDebugWrapper -> {
            dumpInstruction(inst.inst, includeTvmCell, indent)
        }
        is TacDebugStackInst -> {
            append(indent)
            append(inst.mnemonic)
            append("(")
            append(dumpOperands(inst.parameters, includeTvmCell))
            append(")")
            appendLine()
        }
    }
    append(indent)
    append(" // ")
    append(dumpStackState(inst.stackAfter))
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacSetGlobalInst,
    indent: String,
) {
    append(indent)
    append("SETGLOB global_${inst.globalIndex}, ${inst.value.name}")
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacInst,
    includeTvmCell: Boolean,
    indent: String,
) {
    when (inst) {
        is TacOrdinaryInst<*> -> dumpInstruction(inst, includeTvmCell, indent)
        is TacAssignInst -> dumpInstruction(inst, indent)
        is TacGotoInst -> dumpInstruction(inst, indent)
        is TacLabel -> dumpInstruction(inst, indent)
        is TacReturnInst -> dumpInstruction(inst, indent)
        is TacRetaltInst -> dumpInstruction(inst, indent)
        is TacPopCtrInst -> dumpInstruction(inst, indent)
        is TacPushCtrInst -> dumpInstruction(inst, indent)
        is TacSetGlobalInst -> dumpInstruction(inst, indent)
        is TacLoopInst<*> -> dumpInstruction(inst, includeTvmCell, indent)
    }
}

private fun StringBuilder.dumpInstruction(
    inst: TacAssignInst,
    indent: String,
) {
    append(indent)
    append(inst.lhs.name)
    append(" = ")
    append(inst.rhs.name)
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacGotoInst,
    indent: String,
) {
    append(indent)
    append("goto ${inst.label}")
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacLabel,
    indent: String,
) {
    append(indent)
    append("${inst.label}:")
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacReturnInst,
    indent: String,
) {
    append(indent)
    append("return ")
    append(inst.result.joinToString { it.name })
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacRetaltInst,
    indent: String,
) {
    append(indent)
    append("retalt ")
    append(inst.result.joinToString { it.name })
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacLoopInst<*>,
    includeTvmCell: Boolean,
    indent: String,
) {
    append(indent)
    append(inst.mnemonic)
    if (inst.inputs.isNotEmpty()) {
        append("(")
        val stackInputStr = inst.inputs.joinToString { it.name }
        val params =
            listOf(stackInputStr)
                .filter {
                    it.isNotEmpty()
                }.joinToString()
        append(params)
        append(")")
    }

    inst.blocks.forEach {
        appendLine(" {")
        dumpTacCodeBlock(it, includeTvmCell, indent + " ".repeat(INDENT))
        append(indent)
        append("}")
    }
    appendLine()
}

private fun StringBuilder.dumpInstruction(
    inst: TacOrdinaryInst<*>,
    includeTvmCell: Boolean,
    indent: String,
) {
    append(indent)
    if (inst.outputs.isNotEmpty()) {
        append(inst.outputs.joinToString { it.name })
        append(" = ")
    }
    append(inst.mnemonic)
    append("(")
    val operandsStr = dumpOperands(inst.operands, includeTvmCell)
    val stackInputStr = inst.inputs.joinToString { it.name }
    val params =
        listOf(operandsStr, stackInputStr)
            .filter {
                it.isNotEmpty()
            }.joinToString()
    append(params)
    append(")")

    inst.blocks.forEach {
        appendLine(" {")
        dumpTacCodeBlock(it, includeTvmCell, indent + " ".repeat(INDENT))
        append(indent)
        append("}")
    }
    appendLine()
}

private const val INDENT = 4
