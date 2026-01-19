package org.ton.tac

import kotlin.test.assertTrue

fun assertBlockEndsWith(
    tacCode: TacContractCode<AbstractTacInst>,
    parentMnemonic: String,
    lastInstPredicate: (AbstractTacInst) -> Boolean,
) {
    var found = false

    fun scan(instructions: List<AbstractTacInst>) {
        for (inst in instructions) {
            if (inst is TacOrdinaryInst<*>) {
                if (inst.mnemonic == parentMnemonic) {
                    for (block in inst.blocks) {
                        val last = block.lastOrNull()
                        if (last != null && lastInstPredicate(last)) {
                            found = true
                            return
                        }
                    }
                }

                inst.blocks.forEach { scan(it) }
            }
        }
    }

    scan(tacCode.mainMethod.instructions)
    tacCode.methods.values.forEach {
        scan(it.instructions)
    }

    assertTrue(
        found,
        "Did not find instruction $parentMnemonic with a block ending with the expected instruction.",
    )
}
