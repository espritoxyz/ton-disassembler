package org.ton.tac

sealed interface TacDebugInst : AbstractTacInst {
    val stackBefore: List<TacVar>
    val stackAfter: List<TacVar>
}

class TacDebugStackInst(
    val mnemonic: String,
    val parameters: Map<String, Any?>,
    override val stackBefore: List<TacVar>,
    override val stackAfter: List<TacVar>,
) : TacDebugInst

class TacInstDebugWrapper(
    val inst: TacInst,
    override val stackBefore: List<TacVar>,
    override val stackAfter: List<TacVar>
) : TacDebugInst
