package org.ton.tac

import org.ton.bytecode.MethodId
import org.ton.bytecode.TvmContractCode

data class ControlRegisterValue(
    val type: String, // "Continuation", "Cell", "Integer", etc.
    val ref: Int,     // for continuation
    val value: Any? = null // for other types
)

class TacGenerationContext<Inst : AbstractTacInst>(
    val contract: TvmContractCode,
    val debug: Boolean,
) {
    val tupleRegistry = LinkedHashMap<String, List<TacStackValue>>()
    val controlRegisters = mutableMapOf<Int, ControlRegisterValue>()
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
