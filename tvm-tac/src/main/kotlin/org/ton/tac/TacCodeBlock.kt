package org.ton.tac

import java.math.BigInteger

typealias MethodId = BigInteger

sealed class TacCodeBlock {
    abstract val instructions: MutableList<TacInst>
    abstract val methodArgs: List<TacVar>
}

class TacMainMethod(
    override val instructions: MutableList<TacInst>,
    override val methodArgs: List<TacVar> = emptyList()
) : TacCodeBlock()

class TacMethod(
    val methodId: MethodId,
    override val instructions: MutableList<TacInst>,
    override val methodArgs: List<TacVar> = emptyList()
) : TacCodeBlock()

class TacInlineMethod(
    override val instructions: MutableList<TacInst>,
    override val methodArgs: List<TacVar> = emptyList(),
//    val id: String,
//    val parentId: String? = null    - could use that
) : TacCodeBlock()