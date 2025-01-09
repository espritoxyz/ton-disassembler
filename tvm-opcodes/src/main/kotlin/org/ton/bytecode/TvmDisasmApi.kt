package org.ton.bytecode

import org.ton.disasm.TvmDisassembler
import java.nio.file.Path

fun disassembleBoc(bocAsByteArray: ByteArray): TvmContractCode {
    val json = TvmDisassembler.disassemble(bocAsByteArray)
    return TvmContractCode.fromJson(json.toString())
}

fun disassembleBoc(pathToBoc: Path): TvmContractCode {
    val file = pathToBoc.toFile()
    return disassembleBoc(file.readBytes())
}
