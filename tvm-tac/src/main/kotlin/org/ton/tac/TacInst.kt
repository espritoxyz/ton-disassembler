package org.ton.tac

sealed class TacInst(
    open val mnemonic: String
)

data class StackTacInst(
    override val mnemonic: String,
    val stackState: String,
    val operands:  Map<String, Any?>
): TacInst(mnemonic)

data class NonStackTacInst(
    override val mnemonic: String,
    val inputs: List<TacVar>,
    val outputs: List<TacVar>,
    val operands: MutableMap<String, Any?>,
    val contIsolatedsRefs: MutableList<ContinuationRef> = mutableListOf(),  // if CONT was retrieved from stack or from operands
    val contStackPassedRefs: MutableList<ContinuationRef> = mutableListOf(),
    val saveC0: Boolean,
    val instPrefix: String = "",  // for lateinit and mut
    var instSuffix: String = "",  // now only for CALLDICT return stack[]
    var debugInfo: String? = null,
    var warningInfo: String? = null,
) : TacInst(mnemonic)

data class TacVar(
    val name: String,
    var valueTypes: List<String> = listOf(),
    val contRef: ContinuationRef? = null, // if CONT was pushed on a stack
)