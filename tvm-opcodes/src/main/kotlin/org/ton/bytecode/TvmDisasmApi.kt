package org.ton.bytecode

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
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

fun continuationToContractCode(continuation: TvmInstList): TvmContractCode {
    val mainMethod = TvmMainMethod(continuation.list.toMutableList())

    val nonDisassembledCode = TvmContractCode(mainMethod = mainMethod, methods = emptyMap())
    if (continuation.list.size != TvmDisassembler.defaultRootForContinuation.size) {
        return nonDisassembledCode
    }
    if ((continuation.list zip TvmDisassembler.defaultRootForContinuation).any { it.first.mnemonic != it.second }) {
        return nonDisassembledCode
    }

    val dictInst = continuation.list.mapNotNull { it as? TvmDictSpecialDictpushconstInst }.single()
    val dictCell = dictInst.d.toCell()
    val methods = TvmDisassembler.disassembleDictWithMethods(dictCell, dictInst.n)

    val fullJson = JsonObject(
        mapOf(
            "mainMethod" to TvmContractCode.json.encodeToJsonElement(mainMethod),
            "methods" to methods,
        )
    )

    return TvmContractCode.fromJson(fullJson.toString())
}
