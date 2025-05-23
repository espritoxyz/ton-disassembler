package org.ton.bytecode

import org.ton.bitstring.BitString
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import kotlin.reflect.full.memberProperties

fun extractPrimitiveOperands(inst: TvmInst): Map<String, Any?> {
        return inst::class.memberProperties
            .filterNot { it.name in IGNORED_PROPS }
            .associate { it.name to it.getter.call(inst) }
}

fun StringBuilder.printInstruction(inst: TvmInst, indent: String = "", includeTvmCell: Boolean) {
    val type = inst.mnemonic
    val operandsString = extractPrimitiveOperands(inst)
        .entries.joinToString { "${it.key}=" +
                if (includeTvmCell) {
                    formatOperand(it.value)
                } else {
                    if (it.value !is TvmCell) "${it.value}" else "[Cell]"
                }
        }
    appendLine("$indent$type ${operandsString.ifEmpty { "" }}")
}

@OptIn(ExperimentalStdlibApi::class)
private fun formatOperand(value: Any?, indent: String = ""): String {
    return when {
        value is TvmCell -> {
            val cell = Cell(BitString(value.data.bits.map { it == '1' }))
            val hexString = BagOfCells(cell).toByteArray().toHexString()
            val refsFormatted = value.refs.joinToString(System.lineSeparator()) { ref -> formatOperand(ref, "$indent ") }

            val refsString = if (refsFormatted.isNotEmpty()) {
                System.lineSeparator() +
                        indent + "Refs: {" + System.lineSeparator() +
                        refsFormatted + System.lineSeparator() +
                        indent + "}"
            } else {
                ""
            }

            "${indent}Cell($hexString)$refsString"
        }
        else -> value.toString()
    }
}

private val IGNORED_PROPS = TvmInst::class.memberProperties.map { it.name }.toSet() // exclude mnemonic, location and gasConsumption fields