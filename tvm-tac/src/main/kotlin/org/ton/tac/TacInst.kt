package org.ton.tac

sealed interface AbstractTacInst

sealed interface TacInst : AbstractTacInst

data class TacOrdinaryInst<Inst : AbstractTacInst>(
    val mnemonic: String,
    val operands: Map<String, Any?>,
    val inputs: List<TacVar>,
    val outputs: List<TacVar>,
    val blocks: List<List<Inst>>,
) : TacInst

data class TacAssignInst(
    val lhs: TacVar,
    val rhs: TacVar,
) : TacInst

data class TacReturnInst(
    val result: List<TacVar>,
) : TacInst

data class TacGotoInst(
    val label: String,
) : TacInst

data class TacLabel(
    val label: String
) : TacInst

data class TacVar(
    val name: String,
    var valueTypes: List<String> = listOf(),
    val concreteContinuationRef: Int? = null, // if this variable is a concrete continuation
)
