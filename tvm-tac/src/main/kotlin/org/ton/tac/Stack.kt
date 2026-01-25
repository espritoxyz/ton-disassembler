package org.ton.tac

import mu.KLogging
import org.ton.bytecode.TvmRealInst
import org.ton.bytecode.TvmSpecType
import org.ton.bytecode.TvmStackBasicInst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.TvmStackEntryType
import java.util.Collections.swap

val SUPPORTED_STACK_TYPES =
    setOf(
        TvmStackEntryType.SIMPLE,
        TvmStackEntryType.ARRAY,
        TvmStackEntryType.CONST,
        TvmStackEntryType.CONDITIONAL,
    )

internal fun throwErrorIfStackTypesNotSupported(inst: TvmRealInst) {
    if (inst !is TvmStackBasicInst && inst !is TvmStackComplexInst) {
        if (inst.stackInputs == null || inst.stackOutputs == null) {
            error("Instruction: ${inst.mnemonic} is not supported, since stackInputs/Outputs are unconstrained")
        }

        val unsupportedInputTypes =
            inst.stackInputs!!
                .map { it.type }
                .filter { it !in SUPPORTED_STACK_TYPES }

        val unsupportedOutputTypes =
            inst.stackOutputs!!
                .map { it.type }
                .filter { it !in SUPPORTED_STACK_TYPES }

        if (unsupportedInputTypes.isNotEmpty() || unsupportedOutputTypes.isNotEmpty()) {
            error(
                "Unsupported stack value types in ${inst.mnemonic}: " +
                    unsupportedInputTypes.joinToString(", ") +
                    unsupportedOutputTypes.joinToString(", "),
            )
        }
    }
}

internal fun updateStack(
    stackEntriesBefore: List<TacStackValue>,
    methodArgs: List<TacStackValue>,
    stack: Stack,
): Stack {
    val argsSize = methodArgs.size
    if (stackEntriesBefore.size < argsSize) {
        val addedArgsList = methodArgs.takeLast(argsSize - stackEntriesBefore.size)
        val addedArgsValueTypesList = addedArgsList.map { it.valueTypes }
        stack.extendStack(argsSize, addedArgsValueTypesList)
    }
    return stack.copy()
}

data class RegisterState(
    val controlRegisters: MutableMap<Int, TacStackValue> = mutableMapOf(),
    val globalVariables: MutableMap<Int, TacStackValue> = mutableMapOf(),
) {
    fun copy(): RegisterState {
        val newControlRegisters = controlRegisters.toMutableMap()
        val newTupleRegistry = globalVariables.toMutableMap()
        return RegisterState(newControlRegisters, newTupleRegistry)
    }

    fun assignFrom(other: RegisterState) {
        controlRegisters.clear()
        controlRegisters.putAll(other.controlRegisters)

        globalVariables.clear()
        globalVariables.putAll(other.globalVariables)
    }
}

class Stack(
    initialStack: List<TacStackValue>,
) {
    private var argCounter = 0
    private val createdArguments = mutableListOf<TacVar>()
    private val stack: MutableList<TacStackValue> = initialStack.toMutableList()

    val size: Int get() = stack.size

    fun nextVarId(): Int = argCounter++

    fun getCreatedArgs(): List<TacVar> = createdArguments

    fun copy(): Stack {
        val copiedVars = stack.map { it.copy() }
        val newStack = Stack(copiedVars)
        newStack.argCounter = this.argCounter
        newStack.createdArguments.addAll(this.createdArguments)
        return newStack
    }

    fun copyEntries(): List<TacStackValue> = stack.map { it.copy() }

    fun push(value: TacStackValue) {
        stack.add(value)
    }

    fun pop(depth: Int = 0): TacStackValue {
        val requiredSize = depth + 1
        if (stack.size < requiredSize) {
            extendStack(requiredSize)
        }

        if (depth > 0) {
            doReverse(depth + 1, 0)
        }

        val result = popWithTypeCheck(expectedTypes = emptyList())

        if (depth > 0) {
            doReverse(depth, 0)
        }

        return result
    }

    private fun popWithTypeCheck(expectedTypes: List<TvmSpecType>): TacStackValue {
        if (stack.isEmpty()) {
            extendStack(size + 1, listOf(expectedTypes))
        }
        var inputVar: TacStackValue = stack.removeAt(stack.size - 1)

        if (inputVar.valueTypes.isNotEmpty() && expectedTypes.isNotEmpty()) {
            val hasMatchingType = inputVar.valueTypes.any { type -> type in expectedTypes }

            if (inputVar is TacVar && hasMatchingType) {
                val matchedTypes = inputVar.valueTypes.filter { it in expectedTypes }
                inputVar = inputVar.copy(valueTypes = matchedTypes)
                return inputVar
            }

            if (!hasMatchingType) {
                logger.warn {
                    "Type mismatch: var $inputVar: expected one of $expectedTypes, but got ${inputVar.valueTypes}"
                }
            }
            return inputVar
        }

        if (inputVar is TacVar && inputVar.valueTypes.isEmpty() && expectedTypes.isNotEmpty()) {
            inputVar.valueTypes = expectedTypes
        }

        return inputVar
    }

    fun doSwap(
        i: Int,
        j: Int,
    ) {
        val newSize = maxOf(i + 1, j + 1)
        extendStack(newSize)
        swap(stack, stackIndex(i), stackIndex(j))
    }

    /**
     * Reverses the order of s[j+i+1] ... s[j].
     * @param i -- number of stack entries to reverse
     * @param j -- offset before first reversed entry
     */
    fun doReverse(
        i: Int,
        j: Int,
    ) {
        extendStack(i + j)
        val blockStart = stack.size - j
        val reversedBlock = stack.subList(blockStart - i, blockStart).toList()
        reversedBlock.indices.forEach { stack[blockStart - 1 - it] = reversedBlock[it] }
    }

    fun doBlkPush(
        i: Int,
        j: Int,
    ) {
        val newSize = j + 1
        extendStack(newSize)
        repeat(i) {
            val idxJ = stackIndex(j)
            stack.add(stack[idxJ])
        }
    }

    fun doBlkPop(
        i: Int,
        j: Int,
    ) {
        repeat(i) {
            doSwap(0, j)
            stack.removeAt(stack.size - 1)
        }
    }

    fun doBlkSwap(
        i: Int,
        j: Int,
    ) {
        doReverse(i + 1, j + 1)
        doReverse(j + 1, 0)
        doReverse(i + j + 2, 0)
    }

    /**
     * Drops [i] stack elements under the top [j] elements.
     */
    fun doBlkDrop2(
        i: Int,
        j: Int,
    ) {
        val newSize = i + j
        extendStack(newSize)
        val topElements = mutableListOf<TacStackValue>()
        for (k in 0 until newSize) {
            val topElement = stack.last()
            stack.removeAt(size - 1)
            if (k < j) {
                topElements += topElement
            }
        }
        topElements.asReversed().forEach { stack.add(it) }
    }

    fun extendStack(
        newSize: Int,
        listWithValueTypes: List<List<TvmSpecType>> = listOf(),
    ) {
        if (size >= newSize) return

        val newValuesSize = newSize - size
        val newValues =
            (0 until newValuesSize).mapIndexed { idx, _ ->
                TacVar(
                    name = "arg${nextVarId()}",
                    valueTypes = if (listWithValueTypes.isNotEmpty()) listWithValueTypes[idx] else emptyList(),
                )
            }

        createdArguments.addAll(newValues)
        stack.addAll(0, newValues.asReversed())
    }

    fun getAssignInst(
        newVar: TacVar,
        i: Int,
    ): TacAssignInst {
        val value = pop(i)
        val result = TacAssignInst(lhs = newVar, rhs = value)
        push(newVar)
        return result
    }

    private fun stackIndex(i: Int): Int = size - i - 1

    private val logger = object : KLogging() {}.logger
}
