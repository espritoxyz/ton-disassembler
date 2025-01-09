package org.ton.bytecode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvmSubSliceSerializedLoader(
    @SerialName("_bits")
    val bits: List<Int>,
    @SerialName("_refs")
    val refs: List<Int> // todo: refs format
)

interface TvmRefOperandLoader
