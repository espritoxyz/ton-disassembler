package org.ton.tac

sealed interface AbstractTacInst

sealed interface TacInst : AbstractTacInst

data class TacOrdinaryInst<Inst : AbstractTacInst>(
    val mnemonic: String,
    val operands: Map<String, Any?>,
    val inputs: List<TacStackValue>,
    val outputs: List<TacStackValue>,
    val blocks: List<List<Inst>>,
) : TacInst

data class TacAssignInst(
    val lhs: TacVar,
    val rhs: TacStackValue,
) : TacInst

data class TacReturnInst(
    val result: List<TacStackValue>,
) : TacInst

data class TacGotoInst(
    val label: String,
) : TacInst

data class TacLabel(
    val label: String
) : TacInst

sealed interface TacStackValue {
    val valueTypes: List<String>
    val name: String
    fun copy(): TacStackValue
}

data class TacVar(
    override val name: String,
    override var valueTypes: List<String> = listOf(),
) : TacStackValue {
    override fun copy() = TacVar(name, valueTypes)
}

data class ContinuationValue(
    override val name: String,
    val continuationRef: Int,
) : TacStackValue {
    override val valueTypes: List<String>
        get() = listOf("Continuation")

    override fun copy() = this
}
