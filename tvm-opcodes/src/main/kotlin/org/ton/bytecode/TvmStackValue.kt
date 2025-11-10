package org.ton.bytecode

import kotlinx.serialization.Serializable

abstract class TvmStackEntryDescription {
    abstract val type: String
}

@Serializable
data class TvmSimpleStackEntryDescription(
    val name: String,
    val valueTypes: List<String>,
) : TvmStackEntryDescription() {
    override val type: String = "simple"
}

@Serializable
data class TvmConstStackEntryDescription(
    val valueType: String,
    val value: Int? = null,
) : TvmStackEntryDescription() {
    override val type: String = "const"
}

@Serializable
data class TvmArrayStackEntryDescription(
    val name: String,
    val lengthVar: String,
    val arrayEntry: List<TvmSimpleStackEntryDescription>,
) : TvmStackEntryDescription() {
    override val type: String = "array"
}

@Serializable
data class TvmGenericStackEntryDescription(
    override val type: String,
) : TvmStackEntryDescription()
