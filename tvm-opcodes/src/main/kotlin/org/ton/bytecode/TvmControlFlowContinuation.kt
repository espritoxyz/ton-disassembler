package org.ton.bytecode

import kotlinx.serialization.Serializable

@Serializable
data class TvmControlFlowContinuation(
    val type: String,
    val variableName: String? = null, // only in 'type: variable'
    val save: Map<String, TvmControlFlowContinuation>? = null,
)
