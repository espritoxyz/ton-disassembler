    package org.ton.bytecode

    import java.util.Collections.swap

    typealias VarMap = MutableMap<String, Any?>

    data class StackVariable(val specName: String, val producedBy: String = "", var valueTypes: List<String> = listOf())

    class Stack(initialStack: List<StackVariable>, private val messageCollector: MutableList<String> = ArrayList()) {

        private var varCounter = 0
        private var argCounter = 0
        private val createdArguments = mutableListOf<StackVariable>()

        private fun getNextVar(): Int {
            return varCounter++
        }
        private fun getNextArg(): Int {
            return argCounter++
        }

        private val size: Int get() = stack.size

        private fun stackIndex(i: Int): Int = size - i - 1

        private val stack: MutableList<StackVariable> = initialStack.toMutableList()
        fun getMessageCollector(): MutableList<String> = messageCollector
        fun clearMessagesCollector() = messageCollector.clear()

        fun copy(): Stack {
            val newStack = Stack(stack.toList())
            newStack.varCounter = this.varCounter
            newStack.argCounter = this.argCounter
            return newStack
        }

        fun copyEntries(): List<StackVariable> {
            return stack.toList()
        }


        fun dump(): String {
            return stack.joinToString(", ") { it.specName }
        }

        fun pop(valuesCheck: List<String>): StackVariable {
            if (stack.isEmpty()) {
                extendStack(size + 1, valuesCheck)
            }
            val inputVar: StackVariable = stack.removeAt(stack.size - 1)

            if (inputVar.valueTypes.isNotEmpty() && valuesCheck.isNotEmpty()) {
                val hasMatchingType = inputVar.valueTypes.any { it in valuesCheck }
                if (hasMatchingType) {
                    return inputVar
                }
                messageCollector.add("Value type mismatch in var ${inputVar.specName}: expected one of $valuesCheck, but got ${inputVar.valueTypes}")
                return inputVar
            }

            if (inputVar.valueTypes.isEmpty() && valuesCheck.isNotEmpty()) { //populate type
                inputVar.valueTypes = valuesCheck
            }

            return inputVar
        }

        fun push(specName: String = "noName", inst: String, valueTypes: List<String> = listOf()): StackVariable {
            val variable = StackVariable(specName + "_" + getNextVar(), inst, valueTypes)
            stack.add(variable)
            return variable
        }

        fun getCreatedArgs(): List<StackVariable> = createdArguments

        private fun extendStack(newSize: Int, valueTypes: List<String> = listOf()) {
            if (size >= newSize) {
                return
            }

            val newValuesSize = newSize - size
            val newValues = (0 until newValuesSize).map { StackVariable(specName = "arg${getNextArg()}", valueTypes = valueTypes) }

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
                is TvmStackComplexReverseInst -> doReverse(inst.i, inst.j)
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
            val blockEnd = blockStart
            val blockBegin = blockStart - i
            val reversedBlock = stack.subList(blockBegin, blockEnd).reversed()

            for (index in reversedBlock.indices) {
                stack[blockBegin + index] = reversedBlock[index]
            }
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

            val topElements = mutableListOf<StackVariable>()
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
    }



