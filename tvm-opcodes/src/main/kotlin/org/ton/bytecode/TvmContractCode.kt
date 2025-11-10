package org.ton.bytecode

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.ton.bigint.BigIntSerializer
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.disasm.bytecode.InstructionStackArrayValue
import org.ton.disasm.bytecode.InstructionStackConditionalValue
import org.ton.disasm.bytecode.InstructionStackConstValue
import org.ton.disasm.bytecode.InstructionStackSimpleValue
import org.ton.disasm.bytecode.InstructionStackValueDescription

@Serializable
data class TvmContractCode(
    val mainMethod: TvmMainMethod,
    val methods: Map<
        @Serializable(BigIntSerializer::class)
        MethodId,
        TvmMethod,
    >,
) {
    companion object {
        private val defaultSerializationModule: SerializersModule
            get() =
                SerializersModule {
                    polymorphic(TvmInstLocation::class) {
                        subclass(TvmInstMethodLocation::class)
                        subclass(TvmInstLambdaLocation::class)
                        subclass(TvmMainMethodLocation::class)
                    }

                    polymorphic(TvmCodeBlock::class) {
                        subclass(TvmMethod::class)
                        subclass(TvmMainMethod::class)
                    }

                    polymorphic(InstructionStackValueDescription::class) {
                        subclass(InstructionStackSimpleValue::class)
                        subclass(InstructionStackConstValue::class)
                        subclass(InstructionStackArrayValue::class)
                        subclass(InstructionStackConditionalValue::class)
                    }

                    registerTvmInstSerializer()

                    contextual(MethodId::class, BigIntSerializer)
                }

        val json =
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                serializersModule = defaultSerializationModule
            }

        fun fromJson(bytecode: String): TvmContractCode = json.decodeFromString<TvmContractCode>(bytecode)
    }
}

@Serializable
data class TvmInstList(
    val list: List<TvmInst>,
    val raw: TvmCell, // cell that represents this continuation
) {
    companion object {
        val empty = TvmInstList(emptyList(), TvmCell(TvmCellData(""), emptyList()))
    }
}

@Serializable
data class TvmCell(
    @SerialName("_bits")
    val data: TvmCellData,
    @SerialName("_refs")
    val refs: List<TvmCell>,
)

@Serializable(with = TvmCellDataSerializer::class)
data class TvmCellData(
    val bits: String,
)

class TvmCellDataSerializer : KSerializer<TvmCellData> {
    private val listSerializer = ListSerializer(Char.serializer())

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun deserialize(decoder: Decoder): TvmCellData =
        TvmCellData(listSerializer.deserialize(decoder).joinToString(separator = ""))

    override fun serialize(
        encoder: Encoder,
        value: TvmCellData,
    ) {
        listSerializer.serialize(encoder, value.bits.toList())
    }
}

fun TvmCell.toCell(): Cell {
    val children = refs.map { it.toCell() }
    val data = BitString(data.bits.map { it == '1' })
    return Cell(data, *children.toTypedArray())
}
