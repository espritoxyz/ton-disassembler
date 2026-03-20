package org.ton.tac

/**
 * Common interface for TAC simplification passes.
 * Implementations transform a [TacContractCode] and return a simplified version.
 */
interface TacSimplifier {
    fun simplify(contract: TacContractCode<TacInst>): TacContractCode<TacInst>
}

/** Applies all registered simplification passes in order. */
fun simplifyTacContractCode(contract: TacContractCode<TacInst>): TacContractCode<TacInst> {
    val simplifiers: List<TacSimplifier> =
        listOf(
            SelfAssignmentSimplifier(),
            InlineAssignmentSimplifier(),
        )
    return simplifiers.fold(contract) { code, simplifier -> simplifier.simplify(code) }
}

/**
 * Removes trivial self-assignments of the form `X = X`.
 *
 * A [TacAssignInst] is a self-assignment when its [TacAssignInst.lhs] and
 * [TacAssignInst.rhs] refer to the same variable (compared by [TacStackValue.name]).
 */
class SelfAssignmentSimplifier : TacSimplifier {
    override fun simplify(contract: TacContractCode<TacInst>): TacContractCode<TacInst> =
        SelfAssignmentTransformer.transformContractCode(contract)

    private object SelfAssignmentTransformer : TacCodeTransformer {
        override fun transformAssignInst(inst: TacAssignInst): List<TacInst> =
            if (inst.lhs.name == inst.rhs.name) emptyList() else listOf(inst)
    }
}

/**
 * Inlines variables that are assigned exactly once (or multiple times but always
 * to the same value) via [TacAssignInst].
 *
 * For every such variable `v`, every use of `v` is replaced with the right-hand
 * side of the assignment, and the [TacAssignInst] is removed.
 *
 * Example: `var_1 = x; var_2 = var_1; n = ADD(var_2, var_2)` → `n = ADD(x, x)`
 */
class InlineAssignmentSimplifier : TacSimplifier {
    override fun simplify(contract: TacContractCode<TacInst>): TacContractCode<TacInst> {
        var current = contract
        // Iterate until no more substitutions are possible: a previous pass may
        // unify RHS values and unlock new inlining opportunities.
        while (true) {
            val substitutions = collectSubstitutions(current)
            if (substitutions.isEmpty()) return current

            // Transitively resolve chains: var_1 -> x, var_2 -> var_1 becomes var_2 -> x
            val resolved = resolveTransitively(substitutions)
            current = SubstitutionTransformer(resolved).transformContractCode(current)
        }
    }

    // --- analysis ---

    /**
     * Collects a mapping `varName -> rhsValue` for variables that can be inlined.
     * A variable is inlinable when every [TacAssignInst] targeting it assigns the
     * same right-hand side (compared by [TacStackValue.name]).
     */
    private fun collectSubstitutions(contract: TacContractCode<TacInst>): Map<String, TacStackValue> {
        // varName -> (uniqueRhsName, rhsValue, allSame)
        val assignments = mutableMapOf<String, AssignmentInfo>()

        fun visit(instructions: List<TacInst>) {
            for (inst in instructions) {
                if (inst is TacAssignInst && inst.lhs.name != inst.rhs.name) {
                    val varName = inst.lhs.name
                    val existing = assignments[varName]
                    if (existing == null) {
                        assignments[varName] = AssignmentInfo(inst.rhs.name, inst.rhs)
                    } else if (existing.rhsName != inst.rhs.name) {
                        existing.consistent = false
                    }
                }

                @Suppress("UNCHECKED_CAST")
                val nestedBlocks =
                    when (inst) {
                        is TacOrdinaryInst<*> -> inst.blocks as List<List<TacInst>>
                        is TacLoopInst<*> -> inst.blocks as List<List<TacInst>>
                        else -> emptyList()
                    }
                nestedBlocks.forEach { visit(it) }
            }
        }

        visit(contract.mainMethod.instructions)
        contract.methods.values.forEach { visit(it.instructions) }

        return assignments
            .filter { (_, info) -> info.consistent }
            .mapValues { (_, info) -> info.rhs }
    }

    /**
     * Resolves substitution chains so that `a -> b, b -> c` becomes `a -> c, b -> c`.
     * Protects against cycles by limiting resolution depth.
     */
    private fun resolveTransitively(raw: Map<String, TacStackValue>): Map<String, TacStackValue> {
        val resolved = mutableMapOf<String, TacStackValue>()
        for ((varName, _) in raw) {
            var current = raw[varName] ?: continue
            val visited = mutableSetOf(varName)
            while (current is TacVar && current.name in raw && current.name !in visited) {
                visited += current.name
                current = raw.getValue(current.name)
            }
            resolved[varName] = current
        }
        return resolved
    }

    private class AssignmentInfo(
        val rhsName: String,
        val rhs: TacStackValue,
        var consistent: Boolean = true,
    )
}

// --- transformer ---

/**
 * Replaces every reference to a substituted variable with its resolved value
 * and removes dead [TacAssignInst] nodes.
 */
private class SubstitutionTransformer(
    private val substitutions: Map<String, TacStackValue>,
) : TacCodeTransformer {
    override fun transformAssignInst(inst: TacAssignInst): List<TacInst> =
        if (inst.lhs.name in substitutions) {
            emptyList()
        } else {
            listOf(inst.copy(rhs = sub(inst.rhs))).filter {
                it.lhs.name != it.rhs.name
            }
        }

    override fun transformOrdinaryInst(inst: TacOrdinaryInst<*>): List<TacInst> {
        @Suppress("UNCHECKED_CAST")
        val typedInst = inst as TacOrdinaryInst<TacInst>
        return listOf(
            typedInst.copy(
                inputs = typedInst.inputs.map { sub(it) },
                outputs = typedInst.outputs.map { sub(it) },
                blocks = typedInst.blocks.map { transformInstructions(it) },
            ),
        )
    }

    override fun transformLoopInst(inst: TacLoopInst<*>): List<TacInst> {
        @Suppress("UNCHECKED_CAST")
        val typedInst = inst as TacLoopInst<TacInst>
        return listOf(
            typedInst.copy(
                inputs = typedInst.inputs.map { sub(it) },
                blocks = typedInst.blocks.map { transformInstructions(it) },
            ),
        )
    }

    override fun transformPopCtrInst(inst: TacPopCtrInst): List<TacInst> = listOf(inst.copy(value = sub(inst.value)))

    override fun transformPushCtrInst(inst: TacPushCtrInst): List<TacInst> = listOf(inst.copy(value = sub(inst.value)))

    override fun transformSetGlobalInst(inst: TacSetGlobalInst): List<TacInst> =
        listOf(inst.copy(value = sub(inst.value)))

    override fun transformReturnInst(inst: TacReturnInst): List<TacInst> =
        listOf(inst.copy(result = inst.result.map { sub(it) }))

    override fun transformRetaltInst(inst: TacRetaltInst): List<TacInst> =
        listOf(inst.copy(result = inst.result.map { sub(it) }))

    /** Substitute a stack value if it is a variable present in the substitution map. */
    private fun sub(value: TacStackValue): TacStackValue =
        if (value is TacVar && value.name in substitutions) {
            substitutions.getValue(value.name)
        } else {
            value
        }
}
