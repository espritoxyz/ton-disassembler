package org.ton.disasm

import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.printInstruction
import kotlin.reflect.full.memberProperties

fun StringBuilder.prettyPrint(disassembledFile: TvmContractCode, includeTvmCell: Boolean) {
    appendLine("Main method instructions:")
    printInstructions(disassembledFile.mainMethod.instList, includeTvmCell = includeTvmCell)

    appendLine()
    appendLine("Methods instructions:")
    disassembledFile.methods.forEach { (methodId, method) ->
        appendLine()
        appendLine("Method ID: $methodId")
        printInstructions(method.instList, includeTvmCell = includeTvmCell)
    }

}

fun StringBuilder.printInstructions(instList: List<TvmInst>, indent: String = "", includeTvmCell: Boolean) {
    instList.forEach { inst ->
        val type = inst.mnemonic

        if (inst is TvmContOperandInst) {
            val operandInstLists = inst::class.memberProperties
                .mapNotNull { it.getter.call(inst) as? TvmInstList }

            operandInstLists.forEach { operandInstList ->
                appendLine("$indent$type <{")
                printInstructions(operandInstList.list, "$indent  ", includeTvmCell)
                appendLine("$indent}>")
            }
        } else {
            printInstruction(inst, indent, includeTvmCell)
        }
    }
}