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
    val operands: MutableMap<String, Any?>, // (?) to be deleted; actually important for 'LDU' for example
    val continuationsRefs: List<ContinuationRef>? = null,  // if CONT was not pushed on stack i.e. it's in operands like in 'CALLREF'
    val debugInfo: String? = null,
    val warningInfo: String? = null,
    val label: String? = null, // not used for now
    val goto: String? = null, // not used for now
    val skip: Boolean? = null, // skip because this is CONT later used in IFJMP
) : TacInst(mnemonic)

data class TacVar(
    val name: String,
    var valueTypes: List<String> = listOf(),
    val contRef: ContinuationRef? = null, // if CONT was pushed on a stack
)