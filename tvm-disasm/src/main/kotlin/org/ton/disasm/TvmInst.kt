package org.ton.disasm

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import org.ton.cell.Cell

internal open class TvmInst(
    val type: String,
    val location: TvmInstLocation,
    val operands: Map<String, JsonElement>,
    val physicalLocation: TvmPhysicalInstLocation,
) {
    fun toJson(): JsonObject = JsonObject(
        mapOf(
            "type" to JsonPrimitive(type),
            "location" to location.toJson(),
            "physicalLocation" to Json.encodeToJsonElement(physicalLocation)
        ) + operands
    )
}

internal class TvmConstDictInst(
    type: String,
    location: TvmInstLocation,
    operands: Map<String, JsonElement>,
    physicalLocation: TvmPhysicalInstLocation,
    val dict: Map<String, Cell>,
) : TvmInst(type, location, operands, physicalLocation)
