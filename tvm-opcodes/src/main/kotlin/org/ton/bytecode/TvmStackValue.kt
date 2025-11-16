package org.ton.bytecode

import kotlinx.serialization.Serializable

abstract class TvmStackEntryDescription {
    abstract val type: TvmStackEntryType
}

enum class TvmType {
    INT,
    SLICE,
    CELL,
    TUPLE,
    CONTINUATION,
    BUILDER,
    NULL,
    ANY,
}

enum class TvmStackEntryType {
    SIMPLE,
    CONST,
    ARRAY,
    GENERIC,
    CONDITIONAL,
}

@Serializable
data class TvmSimpleStackEntryDescription(
    val name: String,
    val valueTypes: List<TvmType>,
) : TvmStackEntryDescription() {
    override val type: TvmStackEntryType = TvmStackEntryType.SIMPLE
}

@Serializable
data class TvmConstStackEntryDescription(
    val valueType: TvmType,
    val value: Int? = null,
) : TvmStackEntryDescription() {
    override val type: TvmStackEntryType = TvmStackEntryType.CONST
}

@Serializable
data class TvmArrayStackEntryDescription(
    val name: String,
    val lengthVar: String,
    val arrayEntry: List<TvmSimpleStackEntryDescription>,
) : TvmStackEntryDescription() {
    override val type: TvmStackEntryType = TvmStackEntryType.ARRAY
}

@Serializable
data class TvmGenericStackEntryDescription(
    override val type: TvmStackEntryType,
) : TvmStackEntryDescription()
