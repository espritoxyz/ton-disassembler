package org.ton.bytecode

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger

typealias MethodId = BigInteger

// TODO is it a real entity?
@Serializable
sealed class TvmCodeBlock {
    abstract val instList: List<TvmInst>

    protected fun initLocationsCodeBlock() {
        instList.forEach {
            it.location.codeBlock = this
        }
    }
}

@Serializable
@SerialName("TvmMethod")
open class TvmMethod(
    val id: @Contextual MethodId,
    @SerialName("instList")
    private val instListRaw: MutableList<TvmInst>
) : TvmCodeBlock() {
    override val instList: List<TvmInst>
        get() = instListRaw

    init {
        setLocationParents(instListRaw, parent = null)
        initLocationsCodeBlock()
    }

    override fun toString(): String = "TvmMethod(id=$id)"

    private fun setLocationParents(instList: List<TvmInst>, parent: TvmInstLocation?) {
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
                    setLocationParents(it.c, it.location)
                }
                is TvmContOperand2Inst -> {
                    setLocationParents(it.c1, it.location)
                    setLocationParents(it.c2, it.location)
                }
            }
        }
    }
}

// An artificial entity representing instructions in continuation
@Serializable
@SerialName("TvmLambda")
open class TvmLambda(
    @SerialName("instList")
    private val instListRaw: MutableList<TvmInst>
) : TvmCodeBlock() {
    override val instList: List<TvmInst>
        get() = instListRaw

    init {
        initLocationsCodeBlock()
    }
}
