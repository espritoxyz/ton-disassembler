package org.ton.tac

import org.ton.bytecode.MethodId
import org.ton.bytecode.TvmContractCode

class TacGenerationContext(
    val contract: TvmContractCode,
    val debug: Boolean,
) {
    val isolatedContinuations = mutableMapOf<Int, TacInlineMethod<AbstractTacInst>>()
    val calledMethodsSet = mutableSetOf<MethodId>()

    private var contVarCounter = 0
    private var contCounter = 0

    fun nextContVarName(): String {
        return "var_${contVarCounter++}"
    }

    fun nextContinuationId(): Int {
        return contCounter++
    }
}
