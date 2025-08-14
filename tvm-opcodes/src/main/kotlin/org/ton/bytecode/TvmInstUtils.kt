package org.ton.bytecode

import org.ton.boc.BagOfCells
import kotlin.reflect.full.memberProperties

fun extractPrimitiveOperands(inst: TvmInst): Map<String, Any?> {
        return inst::class.memberProperties
            .filterNot { it.name in IGNORED_PROPS }
            .associate { it.name to it.getter.call(inst) }
}

fun formatInstruction(inst: TvmInst, indent: String = "", includeTvmCell: Boolean) : String {
    val type = inst.mnemonic
    val operandsString = extractPrimitiveOperands(inst)
        .entries.joinToString { "${it.key}=" +
                if (includeTvmCell) {
                    formatOperand(it.value)
                } else {
                    if (it.value !is TvmCell) "${it.value}" else "[Cell]"
                }
        }
    return "$indent$type ${operandsString.ifEmpty { "" }}"
}

@OptIn(ExperimentalStdlibApi::class)
fun formatOperand(value: Any?, indent: String = ""): String {
    return when (value) {
        is TvmCell -> {
            val bocBytes = BagOfCells(value.toCell()).toByteArray()
            val cellHexString = bocBytes.toHexString()
            val address = extractTvmAddress(value)
            val cellOrAddress = address ?: cellHexString
            "${indent}Cell($cellOrAddress)"
        }
        else -> value.toString()
    }
}

fun extractTvmAddress(cell: TvmCell): String? {
    if (cell.refs.isNotEmpty()) return null

    val bitString = cell.data.bits
    if (bitString.length != 267) return null

    val prefix = bitString.substring(0, 3)
    val zeros = bitString.substring(3, 11)

    return if (prefix == "100" && zeros == "0".repeat(8)) {
        val addressBits = bitString.substring(11, 267)
        val addressHex = bitStringToHex(addressBits).padStart(64, '0')
        "0:$addressHex"
    } else null
}

fun bitStringToHex(bits: String): String {
    return bits.chunked(4)
        .joinToString("") { it.toInt(2).toString(16) }
}

private val IGNORED_PROPS = buildSet {  // exclude mnemonic, location, physicalLocation and gasConsumption fields
    addAll(TvmInst::class.memberProperties.map { it.name })
    addAll(TvmRealInst::class.memberProperties.map { it.name })
}
