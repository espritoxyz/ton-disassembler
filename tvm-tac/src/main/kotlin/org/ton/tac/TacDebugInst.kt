package org.ton.tac

sealed interface TacDebugInst : AbstractTacInst {
    val stackAfter: List<TacVar>
}

data class TacDebugStackInst(
    val mnemonic: String,
    val parameters: Map<String, Any?>,
    override val stackAfter: List<TacVar>,
) : TacDebugInst

data class TacInstDebugWrapper(
    val inst: TacInst,
    override val stackAfter: List<TacVar>
) : TacDebugInst
