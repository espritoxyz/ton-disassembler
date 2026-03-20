package org.ton.tac

/**
 * Abstract interface for transforming TAC code trees.
 *
 * Each method returns a transformed instruction list. Default implementations
 * recurse into nested blocks and return the instruction unchanged.
 * Subclasses override only the methods they need.
 */
interface TacCodeTransformer {
    fun transformContractCode(contract: TacContractCode<TacInst>): TacContractCode<TacInst> {
        val newMain =
            TacMainMethod(
                instructions = transformInstructions(contract.mainMethod.instructions),
                methodArgs = contract.mainMethod.methodArgs,
            )
        val newMethods =
            contract.methods.mapValues { (_, method) ->
                TacMethod(
                    methodId = method.methodId,
                    instructions = transformInstructions(method.instructions),
                    methodArgs = method.methodArgs,
                )
            }
        return TacContractCode(mainMethod = newMain, methods = newMethods)
    }

    fun transformInstructions(instructions: List<TacInst>): List<TacInst> =
        instructions.flatMap { transformInstruction(it) }

    /**
     * Transforms a single instruction. Returns a list because a transform may
     * remove (empty list) or expand (multiple) an instruction.
     */
    fun transformInstruction(inst: TacInst): List<TacInst> =
        when (inst) {
            is TacOrdinaryInst<*> -> transformOrdinaryInst(inst)
            is TacLoopInst<*> -> transformLoopInst(inst)
            is TacAssignInst -> transformAssignInst(inst)
            is TacPopCtrInst -> transformPopCtrInst(inst)
            is TacPushCtrInst -> transformPushCtrInst(inst)
            is TacSetGlobalInst -> transformSetGlobalInst(inst)
            is TacReturnInst -> transformReturnInst(inst)
            is TacRetaltInst -> transformRetaltInst(inst)
            is TacGotoInst -> transformGotoInst(inst)
            is TacLabel -> transformLabel(inst)
        }

    fun transformOrdinaryInst(inst: TacOrdinaryInst<*>): List<TacInst> {
        @Suppress("UNCHECKED_CAST")
        val typedInst = inst as TacOrdinaryInst<TacInst>
        val newBlocks = typedInst.blocks.map { transformInstructions(it) }
        return listOf(typedInst.copy(blocks = newBlocks))
    }

    fun transformLoopInst(inst: TacLoopInst<*>): List<TacInst> {
        @Suppress("UNCHECKED_CAST")
        val typedInst = inst as TacLoopInst<TacInst>
        val newBlocks = typedInst.blocks.map { transformInstructions(it) }
        return listOf(typedInst.copy(blocks = newBlocks))
    }

    fun transformAssignInst(inst: TacAssignInst): List<TacInst> = listOf(inst)

    fun transformPopCtrInst(inst: TacPopCtrInst): List<TacInst> = listOf(inst)

    fun transformPushCtrInst(inst: TacPushCtrInst): List<TacInst> = listOf(inst)

    fun transformSetGlobalInst(inst: TacSetGlobalInst): List<TacInst> = listOf(inst)

    fun transformReturnInst(inst: TacReturnInst): List<TacInst> = listOf(inst)

    fun transformRetaltInst(inst: TacRetaltInst): List<TacInst> = listOf(inst)

    fun transformGotoInst(inst: TacGotoInst): List<TacInst> = listOf(inst)

    fun transformLabel(inst: TacLabel): List<TacInst> = listOf(inst)
}
