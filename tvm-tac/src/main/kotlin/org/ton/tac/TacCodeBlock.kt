package org.ton.tac

import org.ton.bytecode.MethodId
import org.ton.bytecode.TvmDisasmCodeBlock

sealed class TacCodeBlock<out Inst> {
    abstract val instructions: List<Inst>
    abstract val methodArgs: List<TacVar>
}

data class TacMainMethod<out Inst>(
    override val instructions: List<Inst>,
    override val methodArgs: List<TacVar> = emptyList(),
) : TacCodeBlock<Inst>()

data class TacMethod<out Inst>(
    val methodId: MethodId,
    override val instructions: List<Inst>,
    override val methodArgs: List<TacVar> = emptyList(),
    val returnValues: List<TacVar>,
) : TacCodeBlock<Inst>()

data class TacInlineMethod<out Inst>(
    override val instructions: List<Inst> = emptyList(),
    override val methodArgs: List<TacVar> = emptyList(),
    val originalTvmCode: TvmDisasmCodeBlock?,
    val endingAssignmentStr: String, // for strings like "var0 = a1, var1 = b3 ..." inside continuations branches
//    val id: String,
//    val parentId: String? = null - could use that for 'goto'
) : TacCodeBlock<Inst>()
