package org.ton.disasm.bytecode

import org.ton.disasm.bytecode.CellOperandType.CodeCell
import org.ton.disasm.bytecode.CellOperandType.OrdinaryCell
import org.ton.disasm.bytecode.CellOperandType.SpecialCell

internal enum class CellOperandType {
    OrdinaryCell,
    CodeCell,
    SpecialCell,
}

internal const val DICT_PUSH_CONST_MNEMONIC = "DICTPUSHCONST"
internal const val PFX_DICT_CONST_GET_JMP_MNEMONIC = "PFXDICTCONSTGETJMP"

internal val opcodeToSubSliceOperandType =
    mapOf(
        "PUSHCONT_SHORT" to CodeCell,
        "PUSHCONT" to CodeCell,
        "PUSHSLICE_LONG" to OrdinaryCell,
        "PUSHSLICE" to OrdinaryCell,
        "PUSHSLICE_REFS" to OrdinaryCell,
        "STSLICECONST" to OrdinaryCell,
        "SDBEGINS" to OrdinaryCell,
        "SDBEGINSQ" to OrdinaryCell,
        "DEBUGSTR" to OrdinaryCell,
    )

internal val opcodeToRefOperandType =
    mapOf(
        DICT_PUSH_CONST_MNEMONIC to SpecialCell,
        PFX_DICT_CONST_GET_JMP_MNEMONIC to SpecialCell,
        "CALLREF" to CodeCell,
        "IFELSEREF" to CodeCell,
        "IFREFELSE" to CodeCell,
        "IFJMPREF" to CodeCell,
        "PUSHREFCONT" to CodeCell,
        "JMPREF" to CodeCell,
        "JMPREFDATA" to CodeCell,
        "IFREF" to CodeCell,
        "IFNOTREF" to CodeCell,
        "IFNOTJMPREF" to CodeCell,
        "IFREFELSEREF" to CodeCell,
        "IFBITJMPREF" to CodeCell,
        "IFNBITJMPREF" to CodeCell,
        "PUSHREFSLICE" to OrdinaryCell,
        "PUSHREF" to OrdinaryCell,
        "STREFCONST" to OrdinaryCell,
        "STREF2CONST" to OrdinaryCell,
    )

internal fun validateCellOperandTypes(parsedSpec: InstructionsList) {
    val instList = parsedSpec.instructions
    instList.forEach { inst ->
        val opname = inst.mnemonic
        val operands = inst.bytecode.operands

        val operandTypes = operands.map { it.type }
        val hasRefOperand = operandTypes.any { it == "ref" }
        val hasSubSliceOperand = operandTypes.any { it == "subslice" }

        if (hasRefOperand) {
            check(opname in opcodeToRefOperandType) {
                "$opname has ref operand, but it is absent in opcodeToRefOperandType"
            }
        }
        if (hasSubSliceOperand) {
            check(opname in opcodeToSubSliceOperandType) {
                "$opname has subslice operand, but it is absent in opcodeToSubSliceOperandType"
            }
        }
    }
}
