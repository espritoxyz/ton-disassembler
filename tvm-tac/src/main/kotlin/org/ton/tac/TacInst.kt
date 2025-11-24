package org.ton.tac

import org.ton.bytecode.TvmType

typealias ContinuationId = Int

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

data class TacPopCtrInst(
    val registerIndex: Int,
    val value: TacStackValue,
) : TacInst

data class TacPushCtrInst(
    val registerIndex: Int,
    val value: TacStackValue,
) : TacInst

data class TacSetGlobalInst(
    val globalIndex: Int,
    val value: TacStackValue,
) : TacInst

data class TacReturnInst(
    val result: List<TacStackValue>,
) : TacInst

data class TacGotoInst(
    val label: String,
) : TacInst

data class TacLabel(
    val label: String,
) : TacInst

sealed interface TacStackValue {
    val valueTypes: List<TvmType>
    val name: String

    fun copy(): TacStackValue
}

data class TacVar(
    override val name: String,
    override var valueTypes: List<TvmType> = listOf(),
    var value: Int? = null,
) : TacStackValue {
    override fun copy() = TacVar(name, valueTypes)
}

data class TacTupleValue(
    override val name: String,
    override val valueTypes: List<TvmType> = listOf(TvmType.TUPLE),
    var elements: List<TacStackValue>,
) : TacStackValue {
    override fun copy() = TacTupleValue(name, valueTypes, elements.map { it.copy() })
}

data class ContinuationValue(
    override val name: String,
    val continuationRef: Int,
) : TacStackValue {
    override val valueTypes: List<TvmType>
        get() = listOf(TvmType.CONTINUATION)

    override fun copy() = this
}
