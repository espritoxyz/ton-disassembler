package org.ton.disasm

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TvmDisassemblerTest {
    private val disassembler = TvmDisassembler

    @Test
    fun testLongInt() {
        val path = getResourcePath<TvmDisassemblerTest>("/samples/longint.boc")
        val bytes = path.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        val expectedWithoutPhysicalLocations =
            """
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
        val resultMethods = removePhysicalLocationKey(result.jsonObject["methods"]!!)
        val expectedAsJson = Json.parseToJsonElement(expectedWithoutPhysicalLocations).jsonObject["methods"]!!
        assertEquals(expectedAsJson, resultMethods)

        checkPhysicalLocations(result, "/samples/longint_positions.json")
    }

    @Test
    fun testPumpers() {
        val pumpers =
            getResourcePath<TvmDisassemblerTest>("/samples/EQCV_FsDSymN83YeKZKj_7sgwQHV0jJhCTvX5SkPHHxVOi0D.boc")
        val bytes = pumpers.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        assertTrue { result["methods"]?.jsonObject?.get("0") != null }
    }

    @Test
    fun testHoneypotWallet() {
        val boc =
            getResourcePath<TvmDisassemblerTest>("/samples/contract_EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g.boc")
        val bytes = boc.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        val expectedPath =
            getResourcePath<TvmDisassemblerTest>("/samples/contract_EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g.json")
        val parsedExpected = Json.parseToJsonElement(expectedPath.toFile().readText())
        assertEquals(parsedExpected, removePhysicalLocationKey(result))

        checkPhysicalLocations(result, "/samples/contract_EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g_positions.json")
    }

    @Test
    fun testCheburashkaWallet() {
        val boc = getResourcePath<TvmDisassemblerTest>("/samples/cheburashka_wallet.boc")
        val bytes = boc.toFile().readBytes()
        val result = disassembler.disassemble(bytes)
        val expectedPath = getResourcePath<TvmDisassemblerTest>("/samples/cheburashka_wallet.json")
        val parsedExpected = Json.parseToJsonElement(expectedPath.toFile().readText())
        assertEquals(parsedExpected, removePhysicalLocationKey(result))

        checkPhysicalLocations(result, "/samples/cheburashka_wallet_positions.json")
    }

    @Test
    fun testLibraryCell() {
        val boc = getResourcePath<TvmDisassemblerTest>("/samples/library_cell.boc")
        val bytes = boc.toFile().readBytes()

        assertThrows<IllegalArgumentException> {
            disassembler.disassemble(bytes)
        }
    }

    private inline fun <reified T> getResourcePath(path: String): Path =
        T::class.java
            .getResource(path)
            ?.path
            ?.let { Path(it) }
            ?: error("Resource $path was not found")

    private fun checkPhysicalLocations(
        disasmResult: JsonObject,
        expectedPositionsPath: String,
    ) {
        val path = getResourcePath<TvmDisassemblerTest>(expectedPositionsPath)
        val parsedExpected = Json.parseToJsonElement(path.toFile().readText()).jsonArray.toSet()
        val actual = extractPhysicalLocations(disasmResult).toSet()
        parsedExpected.forEach { elem ->
            val atThisPosition =
                actual.firstOrNull {
                    it.jsonObject["cell"] == elem.jsonObject["cell"] && it.jsonObject["offset"] == elem.jsonObject["offset"]
                }
            assertContains(actual, elem, message = "$elem not found in actual physical location list. At this position: $atThisPosition")
        }
        assertEquals(parsedExpected.size, actual.size)
    }

    private val physicalLocationKey = TvmInst::physicalLocation.name

    private fun extractPhysicalLocations(jsonElement: JsonElement): List<JsonObject> =
        when (jsonElement) {
            is JsonObject -> {
                if (physicalLocationKey in jsonElement.keys) {
                    val cell =
                        jsonElement[physicalLocationKey]!!
                            .jsonObject["cellHashHex"]!!
                            .jsonPrimitive
                            .content
                            .lowercase()
                    val offset = jsonElement[physicalLocationKey]!!.jsonObject["offset"]!!
                    val mnemonic = jsonElement["type"]!!
                    val cur =
                        JsonObject(
                            mapOf(
                                "cell" to JsonPrimitive(cell),
                                "offset" to offset,
                                "inst" to mnemonic,
                            ),
                        )
                    val further =
                        listOfNotNull(jsonElement["c"], jsonElement["c1"], jsonElement["c2"])
                            .flatMap { extractPhysicalLocations(it) }
                    listOf(cur) + further
                } else {
                    jsonElement.values.flatMap { extractPhysicalLocations(it) }
                }
            }

            is JsonArray -> {
                jsonElement.jsonArray.flatMap { extractPhysicalLocations(it) }
            }

            else -> {
                emptyList()
            }
        }

    private fun removePhysicalLocationKey(jsonElement: JsonElement): JsonElement =
        when (jsonElement) {
            is JsonObject -> {
                val newKeys = jsonElement.keys.minus(physicalLocationKey)
                val newMap =
                    newKeys.associateWith { key ->
                        val newElement = removePhysicalLocationKey(jsonElement.jsonObject[key]!!)
                        newElement
                    }
                JsonObject(newMap)
            }

            is JsonArray -> {
                JsonArray(jsonElement.jsonArray.map { removePhysicalLocationKey(it) })
            }

            else -> jsonElement
        }
}
