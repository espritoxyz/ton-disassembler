package org.ton.tac

import org.ton.bytecode.MethodId
import org.ton.bytecode.TvmDisasmCodeBlock

sealed class TacCodeBlock<out Inst> {
    abstract val instructions: List<Inst>
    abstract val methodArgs: List<TacVar>
}

data class TacMainMethod<out Inst>(
    override val instructions: List<Inst>,
    override val methodArgs: List<TacVar>,
) : TacCodeBlock<Inst>()

data class TacMethod<out Inst>(
    val methodId: MethodId,
    override val instructions: List<Inst>,
    override val methodArgs: List<TacVar>,
) : TacCodeBlock<Inst>()

data class TacContinuationInfo<out Inst>(
    override val instructions: List<Inst>,
    override val methodArgs: List<TacVar>,
    val numberOfReturnedValues: Int?,
    val originalTvmCode: TvmDisasmCodeBlock,
) : TacCodeBlock<Inst>()
