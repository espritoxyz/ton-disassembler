package org.ton.bytecode

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger

typealias MethodId = BigInteger

abstract class TvmCodeBlock {
    abstract val instList: List<TvmInst>

    protected fun initLocationsCodeBlock() {
        instList.forEach {
            it.location.codeBlock = this
        }
    }
}

abstract class TvmDisasmCodeBlock : TvmCodeBlock() {
    protected fun setLocationParents(instList: List<TvmInst>, parent: TvmInstLocation?) {
        instList.forEach {
            if (parent != null) {
                check(it.location is TvmInstLambdaLocation) {
                    "unexpected location: ${it.location}"
                }
                (it.location as TvmInstLambdaLocation).parent = parent
            }
            when (it) {
                !is TvmContOperandInst -> {
                    // do nothing
                }
                is TvmContOperand1Inst -> {
                    setLocationParents(it.c.list, it.location)
                }
                is TvmContOperand2Inst -> {
                    setLocationParents(it.c1.list, it.location)
                    setLocationParents(it.c2.list, it.location)
                }
            }
        }
    }
}

@Serializable
@SerialName("TvmInlineBlock")
class TvmInlineBlock(
    @SerialName("instList")
    private val instListRaw: MutableList<TvmInst>
) : TvmDisasmCodeBlock() {

    override val instList: List<TvmInst>
        get() = instListRaw

    init {
        setLocationParents(instListRaw, parent = null)
        initLocationsCodeBlock()
    }

    override fun toString(): String = "TvmInlineBlock(${instList.size} instructions)"
}


@Serializable
@SerialName("TvmMethod")
open class TvmMethod(
    val id: @Contextual MethodId,
    @SerialName("instList")
    private val instListRaw: MutableList<TvmInst>
) : TvmDisasmCodeBlock() {
    override fun toString(): String = "TvmMethod(id=$id)"

    override val instList: List<TvmInst>
        get() = instListRaw

    init {
        setLocationParents(instListRaw, parent = null)
        initLocationsCodeBlock()
    }
}

@Serializable
@SerialName("TvmMainMethod")
class TvmMainMethod(
    @SerialName("instList")
    private val instListRaw: MutableList<TvmInst>,
) : TvmDisasmCodeBlock() {
    override fun toString(): String = "TvmMainMethod"

    override val instList: List<TvmInst>
        get() = instListRaw

    init {
        setLocationParents(instListRaw, parent = null)
        initLocationsCodeBlock()
    }
}
