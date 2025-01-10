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

@Serializable
data class TvmContractCode(
    val mainMethod: TvmInstList,
    val methods: Map<@Serializable(BigIntSerializer::class) MethodId, TvmMethod>
) {
    companion object {
        private val defaultSerializationModule: SerializersModule
            get() = SerializersModule {
                polymorphic(TvmInstLocation::class) {
                    subclass(TvmInstMethodLocation::class)
                    subclass(TvmInstLambdaLocation::class)
                    subclass(TvmMainMethodLocation::class)
                }

                polymorphic(TvmCodeBlock::class) {
                    subclass(TvmMethod::class)
                }

                registerTvmInstSerializer()

                contextual(MethodId::class, BigIntSerializer)
            }

        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            serializersModule = defaultSerializationModule
        }

        fun fromJson(bytecode: String): TvmContractCode {
            return json.decodeFromString<TvmContractCode>(bytecode)
        }
    }
}

@Serializable(with = TvmInstListSerializer::class)
data class TvmInstList(val list: List<TvmInst>): List<TvmInst> by list

class TvmInstListSerializer : KSerializer<TvmInstList> {
    private val listSerializer = ListSerializer(TvmInst.serializer())

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun deserialize(decoder: Decoder): TvmInstList =
        TvmInstList(listSerializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: TvmInstList) {
        listSerializer.serialize(encoder, value.list)
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
data class TvmCellData(val bits: String)

class TvmCellDataSerializer : KSerializer<TvmCellData> {
    private val listSerializer = ListSerializer(Char.serializer())

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun deserialize(decoder: Decoder): TvmCellData =
        TvmCellData(listSerializer.deserialize(decoder).joinToString(separator = ""))

    override fun serialize(encoder: Encoder, value: TvmCellData) {
        listSerializer.serialize(encoder, value.bits.toList())
    }
}
