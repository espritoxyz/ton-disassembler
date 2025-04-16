package org.ton.tac

import org.ton.bytecode.TvmDisasmCodeBlock
import java.math.BigInteger

typealias MethodId = BigInteger

sealed class TacCodeBlock {
    abstract val instructions: List<TacInst>
    abstract val methodArgs: List<TacVar>
}

data class TacMainMethod(
    override val instructions: List<TacInst>,
    override val methodArgs: List<TacVar> = emptyList(),
) : TacCodeBlock()

data class TacMethod(
    val methodId: MethodId,
    override val instructions: List<TacInst>,
    override val methodArgs: List<TacVar> = emptyList(),
    val originalTvmCode: TvmDisasmCodeBlock?,
    val resultStack: List<TacVar>,
) : TacCodeBlock()

data class TacInlineMethod(
    override val instructions: List<TacInst> = emptyList(),
    override val methodArgs: List<TacVar> = emptyList(),
    val originalTvmCode: TvmDisasmCodeBlock?,
    val endingAssignmentStr: String, // for strings like "var0 = a1, var1 = b3 ..." inside continuations branches
//    val id: String,
//    val parentId: String? = null - could use that for 'goto'
) : TacCodeBlock()