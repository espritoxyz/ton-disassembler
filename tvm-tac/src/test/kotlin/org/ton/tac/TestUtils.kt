package org.ton.tac

import org.ton.bytecode.TvmRealInst
import kotlin.reflect.KClass
import kotlin.test.assertTrue

fun assertBlockEndsWith(
    tacCode: TacContractCode<*>,
    parentClass: KClass<out TvmRealInst>,
    lastInstPredicate: (AbstractTacInst) -> Boolean,
) {
    var found = false

    fun scan(instructions: List<AbstractTacInst>) {
        for (inst in instructions) {
            if (inst is TacOrdinaryInst<*>) {
                if (inst.originalInstClass == parentClass) {
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

    @Suppress("UNCHECKED_CAST")
    scan(tacCode.mainMethod.instructions as List<AbstractTacInst>)
    @Suppress("UNCHECKED_CAST")
    tacCode.methods.values.forEach {
        scan(it.instructions as List<AbstractTacInst>)
    }

    assertTrue(
        found,
        "Did not find instruction ${parentClass::class.simpleName} with a block ending with the expected instruction.",
    )
}
