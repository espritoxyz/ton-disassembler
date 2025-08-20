package org.ton.tac

fun traverseTacCode(
    insts: List<AbstractTacInst>,
    block: (AbstractTacInst) -> Unit,
) {
    insts.forEach {
        traverse(it, block)
    }
}

private fun traverse(
    inst: AbstractTacInst,
    block: (AbstractTacInst) -> Unit,
) {
    block(inst)

    if (inst is TacOrdinaryInst<*>) {
        inst.blocks.forEach {
            traverseTacCode(it, block)
        }
    }

    if (inst is TacInstDebugWrapper) {
        traverse(inst.inst, block)
    }
}
