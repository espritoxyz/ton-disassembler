package org.ton.tac

import org.ton.bytecode.TvmAppGlobalGetglobInst
import org.ton.bytecode.TvmAppGlobalSetglobInst
import org.ton.bytecode.TvmArrayStackEntryDescription
import org.ton.bytecode.TvmCellParseLduqInst
import org.ton.bytecode.TvmConstDataInst
import org.ton.bytecode.TvmConstDataPushcontInst
import org.ton.bytecode.TvmConstDataPushcontShortInst
import org.ton.bytecode.TvmConstDataPushrefcontInst
import org.ton.bytecode.TvmConstIntPushint16Inst
import org.ton.bytecode.TvmConstIntPushint4Inst
import org.ton.bytecode.TvmConstIntPushint8Inst
import org.ton.bytecode.TvmConstIntPushintLongInst
import org.ton.bytecode.TvmConstStackEntryDescription
import org.ton.bytecode.TvmContDictCalldictInst
import org.ton.bytecode.TvmContDictCalldictLongInst
import org.ton.bytecode.TvmContRegistersPopctrInst
import org.ton.bytecode.TvmContRegistersPushctrInst
import org.ton.bytecode.TvmDictInst
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
import org.ton.bytecode.TvmStackEntryType
import org.ton.bytecode.TvmTupleTpushInst
import org.ton.bytecode.TvmTupleTupleInst
import org.ton.bytecode.TvmTupleUntupleInst
import org.ton.bytecode.dictInstHasIntegerKey
import org.ton.bytecode.dictInstHasRef
import org.ton.bytecode.getDictHandler

interface TacInstructionHandler {
    fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst>
}

object TacHandlerRegistry {
    fun getHandler(inst: TvmRealInst): TacInstructionHandler =
        when (inst) {
            is TvmConstIntPushint4Inst,
            is TvmConstIntPushint8Inst,
            is TvmConstIntPushint16Inst,
            is TvmConstIntPushintLongInst,
            is TvmStackComplexPushLongInst,
            -> PushIntHandler

            is TvmStackBasicInst,
            is TvmStackComplexInst,
            -> StackMutationHandler

            is TvmTupleTupleInst -> TupleHandler
            is TvmTupleUntupleInst -> UnTupleHandler
            is TvmTupleTpushInst -> TPushHandler

            is TvmAppGlobalSetglobInst -> SetGlobHandler
            is TvmAppGlobalGetglobInst -> GetGlobHandler

            is TvmContRegistersPopctrInst -> PopCtrHandler
            is TvmContRegistersPushctrInst -> PushCtrHandler

            is TvmConstDataInst -> {
                if (inst is TvmConstDataPushcontInst ||
                    inst is TvmConstDataPushcontShortInst ||
                    inst is TvmConstDataPushrefcontInst
                ) {
                    PushContHandler
                } else {
                    DefaultSpecHandler
                }
            }

            is TvmContDictCalldictInst,
            is TvmContDictCalldictLongInst,
            -> CallDictHandler

            is TvmCellParseLduqInst -> LduqHandler

            is TvmDictInst -> getDictHandler(inst)

            else -> DefaultSpecHandler
        }
}

object DefaultSpecHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val inputsSpec = inst.stackInputs ?: emptyList()
        val outputsSpec = inst.stackOutputs ?: emptyList()

        val inputVars = mutableListOf<TacStackValue>()

        inputsSpec.reversed().forEach { inputDesc ->
            val stackVal = stack.pop(0)

            if (stackVal is TacVar && stackVal.valueTypes.isEmpty()) {
                val expectedTypes = parseTypes(inputDesc)
                if (expectedTypes.isNotEmpty() && !expectedTypes.contains(TvmSpecType.ANY)) {
                    stackVal.valueTypes = expectedTypes
                }
            }

            inputVars.add(0, stackVal)
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
                    TvmStackEntryType.ARRAY ->
                        TacTupleValue(
                            name = newName,
                            elements = emptyList(),
                            isStructureKnown = false,
                        )
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

    private fun parseTypes(desc: TvmStackEntryDescription): List<TvmSpecType> =
        when (desc) {
            is TvmSimpleStackEntryDescription -> desc.valueTypes
            is TvmArrayStackEntryDescription -> listOf(TvmSpecType.TUPLE)
            is TvmConstStackEntryDescription -> listOf(desc.valueType)
            else -> emptyList()
        }
}

object PushContHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
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
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val n =
            (inst.operands["n"] as? Number)?.toInt()
                ?: error("CALLDICT missing 'n'")

        val methodId = java.math.BigInteger.valueOf(n.toLong())

        val result =
            processCallDict(
                ctx,
                stack,
                methodId,
                inst,
                inst.operands,
            )

        return listOf(result)
    }
}

object PushIntHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
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
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = emptyList(),
                outputs = listOf(tacVal),
                blocks = emptyList(),
            ),
        )
    }
}

object StackMutationHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
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
                stack.doSwap(0, inst.j)
            }

            is TvmStackComplexRevxInst -> {
                val j = extractInt(stack)
                val i = extractInt(stack)
                stack.doReverse(i, j)
            }

            is TvmStackComplexRotrevInst -> {
                stack.doSwap(1, 2)
                stack.doSwap(0, 2)
            }

            is TvmStackComplexXchgxInst -> {
                val i = extractInt(stack)
                stack.doSwap(0, i)
            }

            is TvmStackComplexPu2xcInst -> {
                stack.doBlkPush(1, inst.i)
                stack.doSwap(0, 1)
                stack.doBlkPush(1, inst.j)
                stack.doSwap(0, 1)
                stack.doSwap(0, inst.k)
            }

            is TvmStackComplexPuxc2Inst -> {
                stack.doBlkPush(1, inst.i)
                stack.doSwap(0, 2)
                stack.doSwap(1, inst.j)
                stack.doSwap(0, inst.k)
            }

            is TvmStackComplexPuxcpuInst -> {
                stack.doBlkPush(1, inst.i)
                stack.doSwap(0, 1)
                stack.doSwap(0, inst.j)
                stack.doBlkPush(1, inst.k)
            }

            is TvmStackComplexXc2puInst -> {
                stack.doSwap(1, inst.i)
                stack.doSwap(0, inst.j)
                stack.doBlkPush(1, inst.k)
            }

            is TvmStackComplexXchg3Inst -> {
                stack.doSwap(2, inst.i)
                stack.doSwap(1, inst.j)
                stack.doSwap(0, inst.k)
            }

            is TvmStackComplexXchg3AltInst -> {
                stack.doSwap(2, inst.i)
                stack.doSwap(1, inst.j)
                stack.doSwap(0, inst.k)
            }

            is TvmStackComplexXcpu2Inst -> {
                stack.doSwap(inst.i, 0)
                stack.doBlkPush(1, inst.j)
                stack.doBlkPush(1, inst.k + 1)
            }

            is TvmStackComplexXcpuxcInst -> {
                stack.doSwap(1, inst.i)
                stack.doBlkPush(1, inst.j)
                stack.doSwap(0, 1)
                stack.doSwap(0, inst.k)
            }

            is TvmStackComplexDepthInst -> TODO("Cannot implement stack depth yet (TvmStackComplexDepthInst)")
            is TvmStackComplexChkdepthInst -> TODO("Cannot implement stack depth yet (TvmStackComplexChkdepthInst)")
            is TvmStackComplexOnlytopxInst -> TODO("??")
            is TvmStackComplexOnlyxInst -> TODO("??")
        }
    }

    private fun extractInt(stack: Stack): Int {
        val value = stack.pop(0)
        if (value is TacIntValue) return value.value.toInt()
        if (value is TacVar && value.value != null) return value.value!!

        error("Stack manipulation instruction requires a constant value, but got: $value")
    }
}

object TupleHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
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
                isStructureKnown = true,
            )

        stack.push(tupleVar)

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
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val value = stack.pop(0)
        val tuple = stack.pop(0)

        val (oldElements, isKnown) =
            if (tuple is TacTupleValue) {
                tuple.elements to tuple.isStructureKnown
            } else {
                emptyList<TacStackValue>() to false
            }

        val newElements = oldElements + value

        val newTuple =
            TacTupleValue(
                name = "t_${ctx.nextVarId()}",
                elements = newElements,
                isStructureKnown = isKnown,
            )

        stack.push(newTuple)

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
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
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
                                TacVar(name = "field_${ctx.nextVarId()}", valueTypes = listOf(TvmSpecType.ANY))
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
                    TacVar(name = "field_${ctx.nextVarId()}", valueTypes = listOf(TvmSpecType.ANY))
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
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val k = (inst.operands["k"] as? Number)?.toInt() ?: error("SETGLOB missing 'k'")
        val value = stack.pop(0)

        registerState.globalVariables[k] = value

        return listOf(TacSetGlobalInst(globalIndex = k, value = value))
    }
}

object GetGlobHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val k = (inst.operands["k"] as? Number)?.toInt() ?: error("GETGLOB missing 'k'")

        val pushValue =
            registerState.globalVariables[k]?.copy()
                ?: TacVar("global_${k}_${ctx.nextVarId()}", listOf(TvmSpecType.ANY))

        stack.push(pushValue)

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = emptyList(),
                outputs = listOf(pushValue),
                blocks = emptyList(),
            ),
        )
    }
}

object PopCtrHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val i = (inst.operands["i"] as? Number)?.toInt() ?: 0
        val value = stack.pop(0)

        registerState.controlRegisters[i] = value

        return listOf(TacPopCtrInst(registerIndex = i, value = value))
    }
}

object PushCtrHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val i = (inst.operands["i"] as? Number)?.toInt() ?: 0

        val originalValue = registerState.controlRegisters[i]
        val pushValue =
            originalValue?.copy()
                ?: TacVar(
                    name = "c${i}_${ctx.nextVarId()}",
                    valueTypes = listOf(TvmSpecType.CELL),
                )

        stack.push(pushValue)

        return listOf(TacPushCtrInst(registerIndex = i, value = pushValue))
    }
}

object DictGetHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val inputs = mutableListOf<TacStackValue>()

        val isIntKey = inst.dictInstHasIntegerKey()
        val hasConstLength = inst.operands.containsKey("n")
        if (isIntKey && !hasConstLength) {
            val n = stack.pop(0)
            enforceType(n, TvmSpecType.INT)
            inputs.add(0, stack.pop(0))
        }

        val key = stack.pop(0)
        val dict = stack.pop(0)

        val keyType = if (isIntKey) TvmSpecType.INT else TvmSpecType.SLICE
        enforceType(key, keyType)

        inputs.add(0, key)
        inputs.add(0, dict)

        val outputs = mutableListOf<TacStackValue>()
        inst.stackOutputs?.forEach { outputDesc ->
            val rawName = if (outputDesc is TvmSimpleStackEntryDescription) outputDesc.name else "val"
            val newName = "${rawName}_${ctx.nextVarId()}"

            val finalType =
                if (rawName == "f") {
                    listOf(TvmSpecType.INT)
                } else {
                    val mainType = if (inst.dictInstHasRef()) TvmSpecType.CELL else TvmSpecType.SLICE
                    listOf(mainType)
                }

            outputs.add(TacVar(newName, finalType))
        }

        outputs.forEach { stack.push(it) }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = inputs,
                outputs = outputs,
                blocks = emptyList(),
            ),
        )
    }
}

object DictSetHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val inputs = mutableListOf<TacStackValue>()

        val isIntKey = inst.dictInstHasIntegerKey()
        val hasConstLength = inst.operands.containsKey("n")

        if (isIntKey && !hasConstLength) {
            val n = stack.pop(0)
            enforceType(n, TvmSpecType.INT)
            inputs.add(0, n)
        }

        val value = stack.pop(0)
        val key = stack.pop(0)
        val dict = stack.pop(0)

        val valueType = if (inst.dictInstHasRef()) TvmSpecType.CELL else TvmSpecType.SLICE
        enforceType(value, valueType)

        val keyType = if (isIntKey) TvmSpecType.INT else TvmSpecType.SLICE
        enforceType(key, keyType)

        enforceType(dict, TvmSpecType.CELL)

        inputs.add(0, value)
        inputs.add(0, key)
        inputs.add(0, dict)

        val outputs = mutableListOf<TacStackValue>()

        inst.stackOutputs?.forEach { outputDesc ->
            val rawName = if (outputDesc is TvmSimpleStackEntryDescription) outputDesc.name else "val"
            val newName = "${rawName}_${ctx.nextVarId()}"

            val type = if (inst.dictInstHasRef()) TvmSpecType.CELL else TvmSpecType.SLICE

            val finalType =
                if (rawName.uppercase().startsWith("D")) {
                    listOf(TvmSpecType.CELL)
                } else {
                    listOf(type)
                }

            outputs.add(TacVar(newName, finalType))
        }

        outputs.forEach { stack.push(it) }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = inputs,
                outputs = outputs,
                blocks = emptyList(),
            ),
        )
    }
}

object DictDelHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val inputs = mutableListOf<TacStackValue>()

        val isIntKey = inst.dictInstHasIntegerKey()
        val hasConstLength = inst.operands.containsKey("n")

        if (isIntKey && !hasConstLength) {
            val n = stack.pop(0)
            enforceType(n, TvmSpecType.INT)
            inputs.add(0, n)
        }

        val key = stack.pop(0)
        val dict = stack.pop(0)

        val keyType = if (isIntKey) TvmSpecType.INT else TvmSpecType.SLICE
        enforceType(key, keyType)
        enforceType(dict, TvmSpecType.CELL)

        inputs.add(0, key)
        inputs.add(0, dict)

        val outputs = mutableListOf<TacStackValue>()

        inst.stackOutputs?.forEach { outputDesc ->
            val rawName = if (outputDesc is TvmSimpleStackEntryDescription) outputDesc.name else "val"
            val newName = "${rawName}_${ctx.nextVarId()}"

            val type =
                when {
                    rawName == "f" -> TvmSpecType.INT
                    inst.dictInstHasRef() -> TvmSpecType.CELL
                    else -> TvmSpecType.SLICE
                }
            val finalType =
                if (outputDesc.type == TvmStackEntryType.SIMPLE && rawName.uppercase().startsWith("D")) {
                    listOf(TvmSpecType.CELL)
                } else {
                    listOf(type)
                }

            outputs.add(TacVar(newName, finalType))
        }

        outputs.forEach { stack.push(it) }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = inputs,
                outputs = outputs,
                blocks = emptyList(),
            ),
        )
    }
}

object DictMinMaxHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val inputs = mutableListOf<TacStackValue>()

        val isIntKey = inst.dictInstHasIntegerKey()
        val hasConstLength = inst.operands.containsKey("n")
        if (isIntKey && !hasConstLength) {
            val n = stack.pop(0)
            enforceType(n, TvmSpecType.INT)
            inputs.add(0, n)
        }

        val dict = stack.pop(0)
        enforceType(dict, TvmSpecType.CELL)
        inputs.add(0, dict)

        val outputs = mutableListOf<TacStackValue>()
        inst.stackOutputs?.forEach { outputDesc ->
            val rawName = if (outputDesc is TvmSimpleStackEntryDescription) outputDesc.name else "val"
            val newName = "${rawName}_${ctx.nextVarId()}"

            val type =
                when {
                    rawName == "f" -> TvmSpecType.INT
                    rawName.uppercase().startsWith("D") -> TvmSpecType.CELL
                    rawName == "k" -> if (isIntKey) TvmSpecType.INT else TvmSpecType.SLICE
                    else -> if (inst.dictInstHasRef()) TvmSpecType.CELL else TvmSpecType.SLICE
                }

            outputs.add(TacVar(newName, listOf(type)))
        }

        outputs.forEach { stack.push(it) }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = inputs,
                outputs = outputs,
                blocks = emptyList(),
            ),
        )
    }
}

object LduqHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val inputs = mutableListOf<TacStackValue>()

        val len = stack.pop(0)
        enforceType(len, TvmSpecType.INT)
        inputs.add(0, len)

        val slice = stack.pop(0)
        enforceType(slice, TvmSpecType.SLICE)
        inputs.add(0, slice)

        val valueVar = TacVar("val_${ctx.nextVarId()}", listOf(TvmSpecType.INT))
        val remainderVar = TacVar("rest_${ctx.nextVarId()}", listOf(TvmSpecType.SLICE))
        val flagVar = TacVar("success_${ctx.nextVarId()}", listOf(TvmSpecType.INT))

        stack.push(valueVar)
        stack.push(remainderVar)
        stack.push(flagVar)

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = inputs,
                outputs = listOf(valueVar, remainderVar, flagVar),
                blocks = emptyList(),
            ),
        )
    }
}

private fun enforceType(
    value: TacStackValue,
    type: TvmSpecType,
) {
    if (value is TacVar && value.valueTypes.isEmpty()) {
        value.valueTypes = listOf(type)
    }
}
