package org.ton.tac

import org.ton.bytecode.TvmConstStackEntryDescription
import org.ton.bytecode.TvmInst
import org.ton.bytecode.TvmSimpleStackEntryDescription
import org.ton.bytecode.TvmStackBasicInst
import org.ton.bytecode.TvmStackBasicNopInst
import org.ton.bytecode.TvmStackBasicPopInst
import org.ton.bytecode.TvmStackBasicPushInst
import org.ton.bytecode.TvmStackBasicXchg0iInst
import org.ton.bytecode.TvmStackBasicXchg0iLongInst
import org.ton.bytecode.TvmStackBasicXchg1iInst
import org.ton.bytecode.TvmStackBasicXchgIjInst
import org.ton.bytecode.TvmStackComplexBlkdrop2Inst
import org.ton.bytecode.TvmStackComplexBlkdropInst
import org.ton.bytecode.TvmStackComplexBlkpushInst
import org.ton.bytecode.TvmStackComplexBlkswapInst
import org.ton.bytecode.TvmStackComplexBlkswxInst
import org.ton.bytecode.TvmStackComplexChkdepthInst
import org.ton.bytecode.TvmStackComplexDepthInst
import org.ton.bytecode.TvmStackComplexDrop2Inst
import org.ton.bytecode.TvmStackComplexDropxInst
import org.ton.bytecode.TvmStackComplexDup2Inst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.TvmStackComplexMinusrollxInst
import org.ton.bytecode.TvmStackComplexOnlytopxInst
import org.ton.bytecode.TvmStackComplexOnlyxInst
import org.ton.bytecode.TvmStackComplexOver2Inst
import org.ton.bytecode.TvmStackComplexPickInst
import org.ton.bytecode.TvmStackComplexPopLongInst
import org.ton.bytecode.TvmStackComplexPu2xcInst
import org.ton.bytecode.TvmStackComplexPush2Inst
import org.ton.bytecode.TvmStackComplexPush3Inst
import org.ton.bytecode.TvmStackComplexPushLongInst
import org.ton.bytecode.TvmStackComplexPuxc2Inst
import org.ton.bytecode.TvmStackComplexPuxcInst
import org.ton.bytecode.TvmStackComplexPuxcpuInst
import org.ton.bytecode.TvmStackComplexReverseInst
import org.ton.bytecode.TvmStackComplexRevxInst
import org.ton.bytecode.TvmStackComplexRollxInst
import org.ton.bytecode.TvmStackComplexRotInst
import org.ton.bytecode.TvmStackComplexRotrevInst
import org.ton.bytecode.TvmStackComplexSwap2Inst
import org.ton.bytecode.TvmStackComplexTuckInst
import org.ton.bytecode.TvmStackComplexXc2puInst
import org.ton.bytecode.TvmStackComplexXchg2Inst
import org.ton.bytecode.TvmStackComplexXchg3AltInst
import org.ton.bytecode.TvmStackComplexXchg3Inst
import org.ton.bytecode.TvmStackComplexXchgxInst
import org.ton.bytecode.TvmStackComplexXcpu2Inst
import org.ton.bytecode.TvmStackComplexXcpuInst
import org.ton.bytecode.TvmStackComplexXcpuxcInst
import org.ton.bytecode.TvmStackEntryDescription
import java.util.Collections.swap

val SUPPORTED_STACK_TYPES = setOf("simple", "const")

internal fun throwErrorIfStackTypesNotSupported(inst: TvmInst) {
    if (inst !is TvmStackBasicInst && inst !is TvmStackComplexInst) {
        if (inst.stackInputs == null || inst.stackOutputs == null) {
            error("Instruction: ${inst.mnemonic} is not supported, since stackInputs/Outputs are unconstrained")
        }

        val unsupportedInputTypes = inst.stackInputs!!
            .map { it.type }
            .filter { it !in SUPPORTED_STACK_TYPES }

        val unsupportedOutputTypes = inst.stackOutputs!!
            .map { it.type }
            .filter { it !in SUPPORTED_STACK_TYPES }

        if (unsupportedInputTypes.isNotEmpty() || unsupportedOutputTypes.isNotEmpty()) {
            error(
                "Unsupported stack value types in ${inst.mnemonic}: " +
                        unsupportedInputTypes.joinToString(", ") +
                        unsupportedOutputTypes.joinToString(", ")
            )
        }
    }
}

internal fun updateStack(
    stackEntriesBefore: List<TacVar>,
    methodArgs: List<TacVar>,
    stack: Stack
) : Stack {
    val argsSize = methodArgs.size
    if (stackEntriesBefore.size < argsSize) {
        val addedArgsList = methodArgs.takeLast(argsSize - stackEntriesBefore.size)
        val addedArgsValueTypesList = addedArgsList.map { it.valueTypes }
        stack.extendStack(argsSize, addedArgsValueTypesList)
    }
    return stack.copy()
}

class Stack(
    initialStack: List<TacVar>,
    private val warningCollector: MutableList<String> = ArrayList(),
) {
    private var varCounter = 0
    private var argCounter = 0
    private val createdArguments = mutableListOf<TacVar>()

    private fun getNextVar(): Int = varCounter++

    private fun getNextArg(): Int = argCounter++

    val size: Int get() = stack.size

    fun copy(): Stack {
        val copiedVars = stack.map { it.copy() }
        val newStack = Stack(copiedVars)
        newStack.varCounter = this.varCounter
        return newStack
    }

    fun copyEntries(): List<TacVar> {
        return stack.map { it.copy() }
    }

    fun dropLastInPlace(i: Int) {
        if (i <= 0) return
        if (i > stack.size) {
            stack.clear()
        } else {
            stack.subList(stack.size - i, stack.size).clear()
        }
    }

    fun addAll(elems: List<TacVar>) {
        stack.addAll(elems)
    }

    private fun stackIndex(i: Int): Int = size - i - 1

    private val stack: MutableList<TacVar> = initialStack.toMutableList()

    private fun getWarningCollector() = warningCollector.joinToString("\n")

    private fun clearMessagesCollector() = warningCollector.clear()

    private fun dumpStackState(): String = stack.joinToString(", ") { it.name }

    private fun pop(
        instName: String,
        valuesCheck: List<String>,
    ): TacVar {
        if (stack.isEmpty()) {
            extendStack(size + 1, listOf(valuesCheck))
        }
        var inputVar: TacVar = stack.removeAt(stack.size - 1)

        if (inputVar.valueTypes.isNotEmpty() && valuesCheck.isNotEmpty()) {
            val hasMatchingType = inputVar.valueTypes.any { type -> type in valuesCheck }

            if (hasMatchingType) {
                val matchedTypes = inputVar.valueTypes.filter { it in valuesCheck }
                inputVar = inputVar.copy(valueTypes = matchedTypes)
                return inputVar
            }
            warningCollector.add(
                "Type mismatch in $instName: var ${inputVar.name}: expected one of $valuesCheck, but got ${inputVar.valueTypes}",
            )
            return inputVar
        }

        if (inputVar.valueTypes.isEmpty() && valuesCheck.isNotEmpty()) { // populate type
            inputVar.valueTypes = valuesCheck
        }

        return inputVar
    }

    private fun push(value: TacVar) {
        stack.add(value)
    }

    fun getCreatedArgs(): List<TacVar> = createdArguments

    fun extendStack(
        newSize: Int,
        listWithValueTypes: List<List<String>> = listOf(),
    ) {
        if (size >= newSize) {
            return
        }

        val newValuesSize = newSize - size
        val newValues = (0 until newValuesSize).mapIndexed { idx, _ ->
            TacVar(
                name = "arg${getNextArg()}",
                valueTypes = if (listWithValueTypes.isNotEmpty()) listWithValueTypes[idx] else emptyList()
            )
        }

        createdArguments.addAll(newValues)
        stack.addAll(0, newValues.asReversed()) // reversed because the "newest" values are at the beginning
    }

    private fun takeLastIntOrThrowTypeError(): Int {
        val lastVar = stack.removeAt(stack.size - 1)
        val intValue = lastVar.valueTypes.find { it == "Int" }
        if (intValue != null) {
            return intValue.toInt()
        } else {
            throw IllegalArgumentException("Expected an Int type but found: ${lastVar.valueTypes}")
        }
    }

    fun execStackInstruction(inst: TvmInst) {
        when (inst) {
            is TvmStackBasicInst -> execBasicStackInstruction(inst)
            is TvmStackComplexInst -> execComplexStackInstruction(inst)
            else -> error("not stack instruction type")
        }
    }

    private fun execBasicStackInstruction(inst: TvmStackBasicInst) {
        when (inst) {
            is TvmStackBasicNopInst -> {} // do nothing
            is TvmStackBasicPushInst -> doPush(inst.i)
            is TvmStackBasicPopInst -> doPop(inst.i)
            is TvmStackBasicXchg0iInst -> doSwap(0, inst.i)
            is TvmStackBasicXchgIjInst -> doSwap(inst.i, inst.j)
            is TvmStackBasicXchg1iInst -> doSwap(1, inst.i)
            is TvmStackBasicXchg0iLongInst -> doSwap(0, inst.i)
        }
    }

    private fun execComplexStackInstruction(inst: TvmStackComplexInst) {
        when (inst) {
            is TvmStackComplexBlkdrop2Inst -> doBlkDrop2(inst.i, inst.j)
            is TvmStackComplexReverseInst -> doReverse(inst.i + 2, inst.j)
            is TvmStackComplexBlkswapInst -> doBlkSwap(inst.i, inst.j)
            is TvmStackComplexRotInst -> doBlkSwap(0, 1)
            is TvmStackComplexBlkdropInst -> doBlkDrop2(inst.i, 0)
            is TvmStackComplexBlkpushInst -> doBlkPush(inst.i, inst.j)
            is TvmStackComplexBlkswxInst -> {
                val j = takeLastIntOrThrowTypeError()
                val i = takeLastIntOrThrowTypeError()
                doBlkSwap(i - 1, j - 1)
            }
            is TvmStackComplexDrop2Inst -> {
                doBlkPop(1, 0)
                doBlkPop(1, 0)
            }
            is TvmStackComplexDropxInst -> {
                val i = takeLastIntOrThrowTypeError()
                doBlkDrop2(i, 0)
            }
            is TvmStackComplexDup2Inst -> {
                doPush(1)
                doPush(1)
            }
            is TvmStackComplexPopLongInst -> doPop(inst.i)
            is TvmStackComplexPush2Inst -> {
                doPush(inst.i)
                doPush(inst.j + 1)
            }
            is TvmStackComplexPush3Inst -> {
                doPush(inst.i)
                doPush(inst.j + 1)
                doPush(inst.k + 2)
            }
            is TvmStackComplexPushLongInst -> {
                doPush(inst.i)
            }
            is TvmStackComplexXchg2Inst -> {
                doXchg2(inst.i, inst.j)
            }
            is TvmStackComplexOver2Inst -> {
                doPush(3)
                doPush(3)
            }
            is TvmStackComplexSwap2Inst -> {
                doBlkSwap(1, 1)
            }
            is TvmStackComplexXcpuInst -> {
                doSwap(inst.i, 0)
                doPush(inst.j)
            }
            is TvmStackComplexTuckInst -> {
                doSwap(0, 1)
                doPush(1)
            }
            is TvmStackComplexMinusrollxInst -> {
                val i = takeLastIntOrThrowTypeError()
                doBlkSwap(i - 1, 0)
            }
            is TvmStackComplexRollxInst -> {
                val i = takeLastIntOrThrowTypeError()
                doBlkSwap(0, i - 1)
            }
            is TvmStackComplexPickInst -> {
                val i = takeLastIntOrThrowTypeError()
                doPush(i)
            }
            is TvmStackComplexPuxcInst -> {
                doPuxc(inst.i, inst.j - 1)
            }
            is TvmStackComplexRevxInst -> {
                val j = takeLastIntOrThrowTypeError()
                val i = takeLastIntOrThrowTypeError()
                doReverse(i, j)
            }
            is TvmStackComplexRotrevInst -> {
                doSwap(1, 2)
                doSwap(0, 2)
            }
            is TvmStackComplexXchgxInst -> {
                val i = takeLastIntOrThrowTypeError()
                doSwap(0, i)
            }
            is TvmStackComplexPu2xcInst -> {
                doPush(inst.i)
                doSwap(0, 1)
                doPuxc(inst.j, inst.k - 1)
            }
            is TvmStackComplexPuxc2Inst -> {
                doPush(inst.i)
                doSwap(0, 2)
                doXchg2(inst.j, inst.k)
            }
            is TvmStackComplexPuxcpuInst -> {
                doPuxc(inst.i, inst.j - 1)
                doPush(inst.k)
            }
            is TvmStackComplexXc2puInst -> {
                doXchg2(inst.i, inst.j)
                doPush(inst.k)
            }
            is TvmStackComplexXchg3Inst -> {
                doXchg3(inst.i, inst.j, inst.k)
            }
            is TvmStackComplexXchg3AltInst -> {
                doXchg3(inst.i, inst.j, inst.k)
            }
            is TvmStackComplexXcpu2Inst -> {
                doSwap(inst.i, 0)
                doPush(inst.j)
                doPush(inst.k + 1)
            }
            is TvmStackComplexXcpuxcInst -> {
                doSwap(1, inst.i)
                doPuxc(inst.j, inst.k - 1)
            }
            is TvmStackComplexDepthInst -> TODO("Cannot implement stack depth yet (TvmStackComplexDepthInst)")
            is TvmStackComplexChkdepthInst -> TODO("Cannot implement stack depth yet (TvmStackComplexChkdepthInst)")
            is TvmStackComplexOnlytopxInst -> TODO("??")
            is TvmStackComplexOnlyxInst -> TODO("??")
        }
    }

    private fun doSwap(i: Int, j: Int) {
        val newSize = maxOf(i + 1, j + 1)
        extendStack(newSize)

        swap(stack, stackIndex(i), stackIndex(j))
    }

    private fun doBlkPush(i: Int, j: Int) {
        val newSize = j + 1
        extendStack(newSize)

        repeat(i) {
            val idxJ = stackIndex(j)
            stack.add(stack[idxJ])
        }
    }

    private fun doBlkPop(i: Int, j: Int) {
        repeat(i) {
            doSwap(0, j)
            stack.removeAt(stack.size - 1)
            }
    }

    /**
     * Reverses the order of s[j+i+1] ... s[j].
     * @param i -- number of stack entries to reverse
     * @param j -- offset before first reversed entry
     * */
    private fun doReverse(i: Int, j: Int) {
        extendStack(i + j)

        val blockStart = stack.size - j
        val reversedBlock = stack.subList(blockStart - i, blockStart).toList()
        reversedBlock.indices.forEach { stack[blockStart - 1 - it] = reversedBlock[it] }
    }

    private fun doPush(i: Int) {
        doBlkPush(1, i)
    }

    private fun doPop(i: Int) {
        doBlkPop(1, i)
    }

    private fun doBlkSwap(i: Int, j: Int) {
        doReverse(i + 1, j + 1)
        doReverse(j + 1, 0)
        doReverse(i + j + 2, 0)
    }

    private fun doXchg2(i: Int, j: Int) {
        doSwap(1, i)
        doSwap(0, j)
    }

    /**
     * Drops [i] stack elements under the top [j] elements.
     */
    private fun doBlkDrop2(i: Int, j: Int) {
        val newSize = i + j
        extendStack(newSize)

        val topElements = mutableListOf<TacVar>()
            for (k in 0 until newSize) {
                val topElement = stack.last()
                stack.removeAt(size - 1)
                if (k < j) {
                    topElements += topElement
                }
            }

        topElements.asReversed().forEach { stack.add(it) }
    }

    private fun doPuxc(i: Int, j: Int) {
        doPush(i)
        doSwap(0, 1)
        doSwap(0, j + 1)
    }

    private fun doXchg3(i: Int, j: Int, k: Int) {
        doSwap(2, i)
        doSwap(1, j)
        doSwap(0, k)
    }

    fun processNonStackInst(
        mnemonic: String,
        stack: Stack,
        inputSpec: List<TvmStackEntryDescription>,
        outputSpec: List<TvmStackEntryDescription>,
        operands: MutableMap<String, Any?>,
        contRef: Int? = null
    ): TacOrdinaryInst {
        val inputs = mutableListOf<TacVar>()
        val outputs = mutableListOf<TacVar>()
        val contRefList = mutableListOf<Int>()
        var constCounter = 0

        // Pop inputs in reverse since we deal with stack
        inputSpec.reversed().forEach { input ->
            if (input.type == "simple") {
                val specInput = input as TvmSimpleStackEntryDescription
                val specValueTypes = specInput.valueTypes
                val poppedValue = stack.pop(instName = mnemonic, valuesCheck = specValueTypes)
                if (poppedValue.concreteContinuationRef != null) {
                    contRefList.add(poppedValue.concreteContinuationRef)
                }
                inputs.add(poppedValue)
            } else {
                error("Unsupported input type: \${input.type}")
            }
        }
        val warningInfo = getWarningCollector()
        stack.clearMessagesCollector()

        outputSpec.forEach { output ->
            when (output.type) {
                "simple" -> {
                    val specOutput = output as TvmSimpleStackEntryDescription
                    val specName = specOutput.name
                    val specValueTypes = specOutput.valueTypes
                    val pushValue = TacVar(
                        name = "${specName}_${getNextVar()}",
                        valueTypes = specValueTypes,
                        concreteContinuationRef = contRef
                    )
                    stack.push(pushValue)
                    outputs.add(pushValue)
                }
                "const" -> {
                    val valueType = listOf((output as TvmConstStackEntryDescription).valueType)
                    val pushValue = TacVar(
                        name = "const${constCounter++}",
                        valueTypes = valueType,
                    )
                    stack.push(pushValue)
                    outputs.add(pushValue)
                }
                else -> error("${output.type} from instruction $mnemonic isn't supported yet")
            }
        }

        return TacOrdinaryInst(
            mnemonic = mnemonic,
            inputs = inputs,
            outputs = outputs,
            operands = operands,
            contIsolatedsRefs = contRefList,
            warningInfo = warningInfo,
            saveC0 = false,
            instPrefix = "",
        )
    }
}



