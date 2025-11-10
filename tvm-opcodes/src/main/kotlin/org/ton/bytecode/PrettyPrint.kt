package org.ton.bytecode

import kotlin.reflect.full.memberProperties

fun TvmContractCode.prettyPrint(includeTvmCell: Boolean): String {
    val sb = StringBuilder()
    sb.appendLine("Main method instructions:")
    appendInstructions(sb, mainMethod.instList, includeTvmCell = includeTvmCell)

    sb.appendLine()
    sb.appendLine("Methods instructions:")
    methods.forEach { (methodId, method) ->
        sb.appendLine()
        sb.appendLine("Method ID: $methodId")
        appendInstructions(sb, method.instList, includeTvmCell = includeTvmCell)
    }

    return sb.toString()
}

private fun appendInstructions(
    sb: StringBuilder,
    instList: List<TvmInst>,
    indent: String = "",
    includeTvmCell: Boolean,
) {
    instList.forEach { inst ->
        val type = inst.mnemonic

        if (inst is TvmContOperandInst) {
            val operandInstLists =
                inst::class
                    .memberProperties
                    .mapNotNull { it.getter.call(inst) as? TvmInstList }

            operandInstLists.forEach { operandInstList ->
                sb.appendLine("$indent$type <{")
                appendInstructions(sb, operandInstList.list, "$indent  ", includeTvmCell)
                sb.appendLine("$indent}>")
            }
        } else {
            sb.appendLine(formatInstruction(inst, indent, includeTvmCell))
        }
    }
}
