package org.ton.tac

sealed interface AbstractTacInst

sealed interface TacInst : AbstractTacInst {
    val mnemonic: String
}

data class TacOrdinaryInst(
    override val mnemonic: String,
    val inputs: List<TacVar>,
    val outputs: List<TacVar>,
    val operands: MutableMap<String, Any?>,
    val contIsolatedsRefs: MutableList<Int> = mutableListOf(),  // if CONT was retrieved from stack or from operands
    val contStackPassedRefs: MutableList<Int> = mutableListOf(),
    val saveC0: Boolean,
    val instPrefix: String = "",  // for lateinit and mut
    var instSuffix: String = "",  // now only for CALLDICT return stack[]
    var warningInfo: String? = null,
) : TacInst

data class TacReturnInst(
    val result: List<TacVar>,
) : TacInst {
    override val mnemonic: String
        get() = "return"
}

data class TacVar(
    val name: String,
    var valueTypes: List<String> = listOf(),
    val concreteContinuationRef: Int? = null, // if this variable is a concrete continuation
)
