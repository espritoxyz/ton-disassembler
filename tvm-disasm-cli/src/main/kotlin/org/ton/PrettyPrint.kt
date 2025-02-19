package org.ton

import org.ton.bytecode.TvmContOperandInst
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmInstList
import org.ton.bytecode.printInstruction
import kotlin.reflect.full.memberProperties

fun prettyPrint(disassembledFile: TvmContractCode, includeTvmCell: Boolean) {
    println("Main method instructions:")
    printInstructions(disassembledFile.mainMethod.instList, includeTvmCell=includeTvmCell)

    println("\nMethods instructions:")
    disassembledFile.methods.forEach { (methodId, method) ->
        println("\nMethod ID: $methodId")
        printInstructions(method.instList, includeTvmCell=includeTvmCell)
    }

}

fun printInstructions(instList: List<TvmInst>, indent: String = "", includeTvmCell: Boolean) {
    instList.forEach { inst ->
        val type = inst.mnemonic

        if (inst is TvmContOperandInst) {
            val operandInstLists = inst::class.memberProperties
                .mapNotNull { it.getter.call(inst) as? TvmInstList }

            operandInstLists.forEach { operandInstList ->
                println("$indent$type <{")
                printInstructions(operandInstList.list, "$indent  ", includeTvmCell)
                println("$indent}>")
            }
        }
        else {
            printInstruction(inst, indent, includeTvmCell)
        }
    }
}