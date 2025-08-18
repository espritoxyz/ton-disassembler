package org.ton.tac

sealed interface EndingInstGenerator<Inst : AbstractTacInst> {
    fun generateEndingInst(ctx: TacGenerationContext<Inst>, stack: Stack): List<Inst>
}

class ReturnInstGenerator<Inst : AbstractTacInst> : EndingInstGenerator<Inst> {
    override fun generateEndingInst(ctx: TacGenerationContext<Inst>, stack: Stack): List<Inst> {
        val values = stack.copyEntries()
        val inst = TacReturnInst(values)

        val resultInst = if (ctx.debug) {
            TacInstDebugWrapper(
                inst = inst,
                stackAfter = values,
            )
        } else {
            inst
        }

        @Suppress("unchecked_cast")
        return listOf(resultInst as Inst)
    }
}

class GotoInstGenerator<Inst : AbstractTacInst>(
    private val outVariables: List<TacVar>,
    private val label: String,
) : EndingInstGenerator<Inst> {
    override fun generateEndingInst(ctx: TacGenerationContext<Inst>, stack: Stack): List<Inst> {
        val assignInsts = outVariables.map { newVar ->
            if (ctx.debug) {
                val inst = getAssignInst(stack, newVar)
                val stackAfter = stack.copyEntries()
                TacInstDebugWrapper(inst, stackAfter)
            } else {
                getAssignInst(stack, newVar)
            }
        }
        val gotoInst = if (ctx.debug) {
            val stackState = stack.copyEntries()
            TacInstDebugWrapper(inst = TacGotoInst(label), stackState)
        } else {
            TacGotoInst(label)
        }
        return (assignInsts + gotoInst).map {
            @Suppress("unchecked_cast")
            it as Inst
        }
    }

    private fun getAssignInst(stack: Stack, newVar: TacVar): TacAssignInst {
        val value = stack.pop(outVariables.size - 1)
        val result = TacAssignInst(lhs = newVar, rhs = value)
        stack.push(newVar)
        return result
    }
}
