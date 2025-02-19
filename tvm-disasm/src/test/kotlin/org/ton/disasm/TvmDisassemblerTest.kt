package org.ton.disasm

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.nio.file.Path
import org.junit.jupiter.api.assertThrows
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class TvmDisassemblerTest {
    private val disassembler = TvmDisassembler

    @Test
    fun testLongInt() {
        val path = getResourcePath<TvmDisassemblerTest>("/samples/longint.boc")
        val bytes = path.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        val expected = """
            {
            "methods": {
              "0": {
               "id": "0",
               "instList": [
                {
                 "type": "PUSHINT_LONG",
                 "location": {
                  "type": "TvmInstMethodLocation",
                  "methodId": "0",
                  "index": 0
                 },
                 "x": "1000000000000000000000000000000000000000000000000000000000000000000010"
                },
                {
                 "type": "XCHG_0I",
                 "location": {
                  "type": "TvmInstMethodLocation",
                  "methodId": "0",
                  "index": 1
                 },
                 "i": 1
                },
                {
                 "type": "ADD",
                 "location": {
                  "type": "TvmInstMethodLocation",
                  "methodId": "0",
                  "index": 2
                 }
                },
                {
                 "type": "POP",
                 "location": {
                  "type": "TvmInstMethodLocation",
                  "methodId": "0",
                  "index": 3
                 },
                 "i": 0
                }
               ]
              }
             }
            }
        """.trimIndent()

        val resultMethods = result.jsonObject["methods"]!!
        val expectedAsJson = Json.parseToJsonElement(expected).jsonObject["methods"]!!
        assertEquals(expectedAsJson, resultMethods)
    }

    @Test
    fun testPumpers() {
        val pumpers = getResourcePath<TvmDisassemblerTest>("/samples/EQCV_FsDSymN83YeKZKj_7sgwQHV0jJhCTvX5SkPHHxVOi0D.boc")
        val bytes = pumpers.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        assertTrue { result["methods"]?.jsonObject?.get("0") != null }
    }

    @Test
    fun testHoneypotWallet() {
        val boc = getResourcePath<TvmDisassemblerTest>("/samples/contract_EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g.boc")
        val bytes = boc.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        val expectedPath = getResourcePath<TvmDisassemblerTest>("/samples/contract_EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g.json")
        val parsedExpected = Json.parseToJsonElement(expectedPath.toFile().readText()).jsonObject["methods"]!!
        val resultMethods = result.jsonObject["methods"]!!
        assertEquals(parsedExpected, resultMethods)
    }

    @Test
    fun testCheburashkaWallet() {
        val boc = getResourcePath<TvmDisassemblerTest>("/samples/cheburashka_wallet.boc")
        val bytes = boc.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        val expectedPath = getResourcePath<TvmDisassemblerTest>("/samples/cheburashka_wallet.json")
        val parsedExpected = Json.parseToJsonElement(expectedPath.toFile().readText()).jsonObject["methods"]!!
        val resultMethods = result.jsonObject["methods"]!!
        assertEquals(parsedExpected, resultMethods)
    }

    @Test
    fun testLibraryCell() {
        val boc = getResourcePath<TvmDisassemblerTest>("/samples/library_cell.boc")
        val bytes = boc.toFile().readBytes()

        assertThrows<IllegalArgumentException> {
            disassembler.disassemble(bytes)
        }
    }

    private inline fun <reified T> getResourcePath(path: String): Path {
        return T::class.java.getResource(path)?.path?.let { Path(it) }
            ?: error("Resource $path was not found")
    }
}
