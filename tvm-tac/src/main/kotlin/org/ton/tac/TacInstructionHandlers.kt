package org.ton.tac

import org.ton.bytecode.TvmArrayStackEntryDescription
import org.ton.bytecode.TvmConstStackEntryDescription
import org.ton.bytecode.TvmRealInst
import org.ton.bytecode.TvmSimpleStackEntryDescription
import org.ton.bytecode.TvmSpecType
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
import org.ton.bytecode.TvmStackComplexDrop2Inst
import org.ton.bytecode.TvmStackComplexDropxInst
import org.ton.bytecode.TvmStackComplexDup2Inst
import org.ton.bytecode.TvmStackComplexInst
import org.ton.bytecode.TvmStackComplexMinusrollxInst
import org.ton.bytecode.TvmStackComplexOver2Inst
import org.ton.bytecode.TvmStackComplexPickInst
import org.ton.bytecode.TvmStackComplexPopLongInst
import org.ton.bytecode.TvmStackComplexPush2Inst
import org.ton.bytecode.TvmStackComplexPush3Inst
import org.ton.bytecode.TvmStackComplexPushLongInst
import org.ton.bytecode.TvmStackComplexPuxcInst
import org.ton.bytecode.TvmStackComplexReverseInst
import org.ton.bytecode.TvmStackComplexRollxInst
import org.ton.bytecode.TvmStackComplexRotInst
import org.ton.bytecode.TvmStackComplexSwap2Inst
import org.ton.bytecode.TvmStackComplexTuckInst
import org.ton.bytecode.TvmStackComplexXchg2Inst
import org.ton.bytecode.TvmStackComplexXcpuInst
import org.ton.bytecode.TvmStackEntryDescription
import org.ton.bytecode.TvmStackEntryType

interface TacInstructionHandler {
    fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst>
}

object TacHandlerRegistry {
    private val specificHandlers =
        mapOf(
            "CALLDICT" to CallDictHandler,
            "CALLDICT_LONG" to CallDictHandler,
            "PUSHCONT" to PushContHandler,
            "PUSHCONT_SHORT" to PushContHandler,
            "PUSHCONT_LONG" to PushContHandler,
            "PUSHINT" to PushIntHandler,
            "PUSHINTLONG" to PushIntHandler,
            "TUPLE" to TupleHandler,
            "TPUSH" to TPushHandler,
            "UNTUPLE" to UnTupleHandler,
            "SETGLOB" to SetGlobHandler,
            "POPCTR" to PopCtrHandler,
            "PUSHCTR" to PushCtrHandler,
        )

    fun getHandler(inst: TvmRealInst): TacInstructionHandler {
        specificHandlers[inst.mnemonic]?.let { return it }

        if (inst is TvmStackBasicInst || inst is TvmStackComplexInst) {
            return StackMutationHandler
        }

        return DefaultSpecHandler
    }
}

object DefaultSpecHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val inputsSpec = inst.stackInputs ?: emptyList()
        val outputsSpec = inst.stackOutputs ?: emptyList()

        val inputVars = mutableListOf<TacStackValue>()

        inputsSpec.reversed().forEach { _ ->
            inputVars.add(0, stack.pop(0))
        }

        val outputVars = mutableListOf<TacStackValue>()

        outputsSpec.forEach { outputDesc ->
            val rawName =
                when (outputDesc) {
                    is TvmSimpleStackEntryDescription -> outputDesc.name
                    is TvmArrayStackEntryDescription -> outputDesc.name
                    is TvmConstStackEntryDescription -> "const"
                    else -> "val"
                }

            val newName = "${rawName}_${ctx.nextVarId()}"
            val newVal =
                when (outputDesc.type) {
                    TvmStackEntryType.ARRAY -> TacTupleValue(name = newName, elements = emptyList())
                    TvmStackEntryType.CONST -> {
                        val constDesc = outputDesc as TvmConstStackEntryDescription
                        val longVal = constDesc.value?.toString()?.toLongOrNull()
                        if (longVal != null) {
                            TacIntValue(newName, longVal)
                        } else {
                            TacVar(newName, valueTypes = parseTypes(outputDesc))
                        }
                    }
                    else -> TacVar(name = newName, valueTypes = parseTypes(outputDesc))
                }

            stack.push(newVal)
            outputVars.add(newVal)

            if (newVal is TacTupleValue) {
                registerState.tupleRegistry[newName] = newVal.elements
            }
        }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = inputVars,
                outputs = outputVars,
                blocks = emptyList(),
            ),
        )
    }

    private fun parseTypes(desc: TvmStackEntryDescription): List<TvmSpecType> {
        if (desc is TvmSimpleStackEntryDescription) {
            return desc.valueTypes
        }
        return emptyList()
    }
}

object PushContHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val info = extractOperandContinuations(ctx, inst)

        val contId =
            info.resultContinuationId
                ?: error("PUSHCONT instruction ${inst.mnemonic} failed to produce continuation ID")

        val contValue =
            ContinuationValue(
                name = "cont_${ctx.nextVarId()}",
                continuationRef = contId,
            )

        stack.push(contValue)

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = emptyList(),
                outputs = listOf(contValue),
                blocks = emptyList(),
            ),
        )
    }
}

object CallDictHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val n =
            (inst.operands["n"] as? Number)?.toInt()
                ?: error("CALLDICT missing 'n'")

        val methodId = java.math.BigInteger.valueOf(n.toLong())

        @Suppress("UNCHECKED_CAST")
        val result =
            processCallDict(
                ctx as TacGenerationContext<AbstractTacInst>,
                stack,
                methodId,
                inst,
                inst.operands,
            )

        return listOf(result)
    }
}

object PushIntHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val value =
            (inst.operands["i"] as? Number)?.toLong()
                ?: (inst.operands["val"] as? String)?.toLongOrNull()
                ?: 0L

        val tacVal =
            TacIntValue(
                name = "const_${ctx.nextVarId()}",
                value = value,
            )

        stack.push(tacVal)

        return listOf(
            TacPushCtrInst(registerIndex = 0, value = tacVal),
        )
    }
}

object StackMutationHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        when (inst) {
            is TvmStackBasicInst -> handleBasic(inst, stack)
            is TvmStackComplexInst -> handleComplex(inst, stack)
            else -> error("StackMutationHandler should only receive stack instructions, but got: ${inst.mnemonic}")
        }

        return emptyList()
    }

    private fun handleBasic(
        inst: TvmStackBasicInst,
        stack: Stack,
    ) {
        when (inst) {
            is TvmStackBasicNopInst -> {}
            is TvmStackBasicPushInst -> stack.doBlkPush(1, inst.i)
            is TvmStackBasicPopInst -> stack.doBlkPop(1, inst.i)
            is TvmStackBasicXchg0iInst -> stack.doSwap(0, inst.i)
            is TvmStackBasicXchgIjInst -> stack.doSwap(inst.i, inst.j)
            is TvmStackBasicXchg1iInst -> stack.doSwap(1, inst.i)
            is TvmStackBasicXchg0iLongInst -> stack.doSwap(0, inst.i)
        }
    }

    private fun handleComplex(
        inst: TvmStackComplexInst,
        stack: Stack,
    ) {
        when (inst) {
            is TvmStackComplexBlkdrop2Inst -> stack.doBlkDrop2(inst.i, inst.j)
            is TvmStackComplexReverseInst -> stack.doReverse(inst.i + 2, inst.j)
            is TvmStackComplexBlkswapInst -> stack.doBlkSwap(inst.i, inst.j)
            is TvmStackComplexRotInst -> stack.doBlkSwap(0, 1)
            is TvmStackComplexBlkdropInst -> stack.doBlkDrop2(inst.i, 0)
            is TvmStackComplexBlkpushInst -> stack.doBlkPush(inst.i, inst.j)
            is TvmStackComplexBlkswxInst -> {
                val j = extractInt(stack)
                val i = extractInt(stack)
                stack.doBlkSwap(i - 1, j - 1)
            }
            is TvmStackComplexDrop2Inst -> {
                stack.doBlkPop(1, 0)
                stack.doBlkPop(1, 0)
            }
            is TvmStackComplexDropxInst -> {
                val i = extractInt(stack)
                stack.doBlkDrop2(i, 0)
            }
            is TvmStackComplexDup2Inst -> {
                stack.doBlkPush(1, 1)
                stack.doBlkPush(1, 1)
            }
            is TvmStackComplexPopLongInst -> stack.doBlkPop(1, inst.i)
            is TvmStackComplexPush2Inst -> {
                stack.doBlkPush(1, inst.i)
                stack.doBlkPush(1, inst.j + 1)
            }
            is TvmStackComplexPush3Inst -> {
                stack.doBlkPush(1, inst.i)
                stack.doBlkPush(1, inst.j + 1)
                stack.doBlkPush(1, inst.k + 2)
            }
            is TvmStackComplexPushLongInst -> {
                stack.doBlkPush(1, inst.i)
            }
            is TvmStackComplexXchg2Inst -> {
                stack.doSwap(1, inst.i)
                stack.doSwap(0, inst.j)
            }
            is TvmStackComplexOver2Inst -> {
                stack.doBlkPush(1, 3)
                stack.doBlkPush(1, 3)
            }
            is TvmStackComplexSwap2Inst -> stack.doBlkSwap(1, 1)
            is TvmStackComplexXcpuInst -> {
                stack.doSwap(inst.i, 0)
                stack.doBlkPush(1, inst.j)
            }
            is TvmStackComplexTuckInst -> {
                stack.doSwap(0, 1)
                stack.doBlkPush(1, 1)
            }
            is TvmStackComplexMinusrollxInst -> {
                val i = extractInt(stack)
                stack.doBlkSwap(i - 1, 0)
            }
            is TvmStackComplexRollxInst -> {
                val i = extractInt(stack)
                stack.doBlkSwap(0, i - 1)
            }
            is TvmStackComplexPickInst -> {
                val i = extractInt(stack)
                stack.doBlkPush(1, i)
            }
            is TvmStackComplexPuxcInst -> {
                stack.doBlkPush(1, inst.i)
                stack.doSwap(0, 1)
                stack.doSwap(0, inst.j + 1)
            }
            else -> {}
        }
    }

    private fun extractInt(stack: Stack): Int {
        val value = stack.pop(0)
        if (value is TacIntValue) return value.value.toInt()
        return 0
    }
}

object TupleHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val size =
            (inst.operands["n"] as? Number)?.toInt()
                ?: error("TUPLE instruction missing 'n' operand")

        val elements = mutableListOf<TacStackValue>()
        repeat(size) {
            elements.add(stack.pop(0))
        }
        elements.reverse()

        val tupleVar =
            TacTupleValue(
                name = "t_${ctx.nextVarId()}",
                elements = elements,
            )

        stack.push(tupleVar)
        registerState.tupleRegistry[tupleVar.name] = elements

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = elements,
                outputs = listOf(tupleVar),
                blocks = emptyList(),
            ),
        )
    }
}

object TPushHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val value = stack.pop(0)
        val tuple = stack.pop(0)

        val oldElements =
            if (tuple is TacTupleValue) {
                tuple.elements
            } else {
                registerState.tupleRegistry[tuple.name] ?: emptyList()
            }

        val newElements = oldElements + value

        val newTuple =
            TacTupleValue(
                name = "t_${ctx.nextVarId()}",
                elements = newElements,
            )

        stack.push(newTuple)

        registerState.tupleRegistry[newTuple.name] = newElements

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = listOf(tuple, value),
                outputs = listOf(newTuple),
                blocks = emptyList(),
            ),
        )
    }
}

object UnTupleHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val tupleVal = stack.pop(0)

        val expectedSize =
            (inst.operands["n"] as? Number)?.toInt()
                ?: error("UNTUPLE instruction missing 'n' operand")

        val outputElements: List<TacStackValue> =
            if (tupleVal is TacTupleValue) {
                if (tupleVal.elements.size != expectedSize) {
                    if (tupleVal.elements.isEmpty()) {
                        val generated =
                            List(expectedSize) {
                                TacVar(name = "field_${ctx.nextVarId()}", valueTypes = listOf(TvmSpecType.FORGOT_ANY))
                            }
                        tupleVal.elements = generated
                        generated
                    } else {
                        error("Tuple size mismatch: got ${tupleVal.elements.size}, expected $expectedSize")
                    }
                } else {
                    tupleVal.elements
                }
            } else {
                List(expectedSize) {
                    TacVar(name = "field_${ctx.nextVarId()}", valueTypes = listOf(TvmSpecType.FORGOT_ANY))
                }
            }

        outputElements.forEach { stack.push(it) }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = listOf(tupleVal),
                outputs = outputElements,
                blocks = emptyList(),
            ),
        )
    }
}

object SetGlobHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val k = (inst.operands["k"] as? Number)?.toInt() ?: error("SETGLOB missing 'k'")
        val value = stack.pop(0)

        if (value is TacTupleValue) {
            registerState.tupleRegistry["global_$k"] = value.elements
        }

        return listOf(TacSetGlobalInst(globalIndex = k, value = value))
    }
}

object PopCtrHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val i = (inst.operands["i"] as? Number)?.toInt() ?: 0
        val value = stack.pop(0)

        registerState.controlRegisters[i] = value

        return listOf(TacPopCtrInst(registerIndex = i, value = value))
    }
}

object PushCtrHandler : TacInstructionHandler {
    override fun handle(
        ctx: TacGenerationContext<*>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<AbstractTacInst> {
        val i = (inst.operands["i"] as? Number)?.toInt() ?: 0

        val originalValue = registerState.controlRegisters[i]
        val pushValue =
            originalValue?.copy()
                ?: TacVar(
                    name = "c${i}_${ctx.nextVarId()}",
                    valueTypes = listOf(TvmSpecType.CELL),
                )

        if (pushValue is TacTupleValue) {
            registerState.tupleRegistry[pushValue.name] = pushValue.elements
        }

        stack.push(pushValue)

        return listOf(TacPushCtrInst(registerIndex = i, value = pushValue))
    }
}
