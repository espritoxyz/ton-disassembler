package org.ton

import org.ton.bytecode.*
import kotlin.reflect.full.memberProperties

fun prettyPrint(disassembledFile: TvmContractCode) {
    println("Main method instructions:")
    printInstructions(disassembledFile.mainMethod.instList)

    println("\nMethods instructions:")
    disassembledFile.methods.forEach { (methodId, method) ->
        println("\nMethod ID: $methodId")
        printInstructions(method.instList)
    }

}

fun printInstructions(instList: List<TvmInst>, indent: String = "") {
    instList.forEach { inst ->
        val type = inst.mnemonic
        val operandsString = extractOperands(inst)

        when (val firstOperand = inst::class.memberProperties
            .firstOrNull { it.name !in IGNORED_PROPS }
            ?.getter?.call(inst)
        ) {
            is TvmInstList -> {
                println("$indent$type <{")
                printInstructions(firstOperand.list, indent + "  ")
                println("$indent}>")
            }
            else -> println("$indent$type ${operandsString.takeIf { it.isNotEmpty() } ?: ""}")
        }
    }
}

private fun extractOperands(inst: TvmInst): String {
    return inst::class.memberProperties
        .filterNot { it.name in IGNORED_PROPS }
        .associate { it.name to it.getter.call(inst) }
        .filterValues { it !is TvmCell }
        .entries.joinToString { "${it.key}=${it.value}" }
}

private val IGNORED_PROPS = setOf("mnemonic", "location", "gasConsumption")
