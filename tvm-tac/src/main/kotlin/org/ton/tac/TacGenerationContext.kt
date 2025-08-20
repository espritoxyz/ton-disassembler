package org.ton.tac

import org.ton.bytecode.MethodId
import org.ton.bytecode.TvmContractCode

class TacGenerationContext<Inst : AbstractTacInst>(
    val contract: TvmContractCode,
    val debug: Boolean,
) {
    val isolatedContinuations = mutableMapOf<Int, TacContinuationInfo<Inst>>()
    val methodsWithSubstitutedStack = mutableMapOf<Int, TacContinuationInfo<Inst>>()
    val calledMethodsSet = mutableSetOf<MethodId>()

    private var varCounter = 0
    private var contCounter = 0
    private var labelCounter = 0

    fun nextVarName(): String {
        return "var_${varCounter++}"
    }

    fun nextVarId(): Int {
        return varCounter++
    }

    fun nextContinuationId(): Int {
        return contCounter++
    }

    fun nextLabel(): String {
        return "label_${labelCounter++}"
    }
}
