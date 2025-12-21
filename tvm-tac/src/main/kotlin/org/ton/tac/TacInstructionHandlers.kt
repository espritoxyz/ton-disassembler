package org.ton.tac

import org.ton.bytecode.TvmAppGlobalGetglobInst
import org.ton.bytecode.TvmAppGlobalSetglobInst
import org.ton.bytecode.TvmArrayStackEntryDescription
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
import org.ton.bytecode.TvmDictDeleteDictdelInst
import org.ton.bytecode.TvmDictDeleteDictdelgetInst
import org.ton.bytecode.TvmDictDeleteDictdelgetrefInst
import org.ton.bytecode.TvmDictDeleteDictidelInst
import org.ton.bytecode.TvmDictDeleteDictidelgetInst
import org.ton.bytecode.TvmDictDeleteDictidelgetrefInst
import org.ton.bytecode.TvmDictDeleteDictudelInst
import org.ton.bytecode.TvmDictDeleteDictudelgetInst
import org.ton.bytecode.TvmDictDeleteDictudelgetrefInst
import org.ton.bytecode.TvmDictGetDictgetInst
import org.ton.bytecode.TvmDictGetDictgetrefInst
import org.ton.bytecode.TvmDictGetDictigetInst
import org.ton.bytecode.TvmDictGetDictigetrefInst
import org.ton.bytecode.TvmDictGetDictugetInst
import org.ton.bytecode.TvmDictGetDictugetrefInst
import org.ton.bytecode.TvmDictMayberefDictgetoptrefInst
import org.ton.bytecode.TvmDictMayberefDictigetoptrefInst
import org.ton.bytecode.TvmDictMayberefDictisetgetoptrefInst
import org.ton.bytecode.TvmDictMayberefDictsetgetoptrefInst
import org.ton.bytecode.TvmDictMayberefDictugetoptrefInst
import org.ton.bytecode.TvmDictMayberefDictusetgetoptrefInst
import org.ton.bytecode.TvmDictMinDictimaxInst
import org.ton.bytecode.TvmDictMinDictimaxrefInst
import org.ton.bytecode.TvmDictMinDictiminInst
import org.ton.bytecode.TvmDictMinDictiminrefInst
import org.ton.bytecode.TvmDictMinDictiremmaxInst
import org.ton.bytecode.TvmDictMinDictiremmaxrefInst
import org.ton.bytecode.TvmDictMinDictiremminInst
import org.ton.bytecode.TvmDictMinDictiremminrefInst
import org.ton.bytecode.TvmDictMinDictmaxInst
import org.ton.bytecode.TvmDictMinDictmaxrefInst
import org.ton.bytecode.TvmDictMinDictminInst
import org.ton.bytecode.TvmDictMinDictminrefInst
import org.ton.bytecode.TvmDictMinDictremmaxInst
import org.ton.bytecode.TvmDictMinDictremmaxrefInst
import org.ton.bytecode.TvmDictMinDictremminInst
import org.ton.bytecode.TvmDictMinDictremminrefInst
import org.ton.bytecode.TvmDictMinDictumaxInst
import org.ton.bytecode.TvmDictMinDictumaxrefInst
import org.ton.bytecode.TvmDictMinDictuminInst
import org.ton.bytecode.TvmDictMinDictuminrefInst
import org.ton.bytecode.TvmDictMinDicturemmaxInst
import org.ton.bytecode.TvmDictMinDicturemmaxrefInst
import org.ton.bytecode.TvmDictMinDicturemminInst
import org.ton.bytecode.TvmDictMinDicturemminrefInst
import org.ton.bytecode.TvmDictNextDictgetnextInst
import org.ton.bytecode.TvmDictNextDictgetnexteqInst
import org.ton.bytecode.TvmDictNextDictgetprevInst
import org.ton.bytecode.TvmDictNextDictgetpreveqInst
import org.ton.bytecode.TvmDictNextDictigetnextInst
import org.ton.bytecode.TvmDictNextDictigetnexteqInst
import org.ton.bytecode.TvmDictNextDictigetprevInst
import org.ton.bytecode.TvmDictNextDictigetpreveqInst
import org.ton.bytecode.TvmDictNextDictugetnextInst
import org.ton.bytecode.TvmDictNextDictugetnexteqInst
import org.ton.bytecode.TvmDictNextDictugetprevInst
import org.ton.bytecode.TvmDictNextDictugetpreveqInst
import org.ton.bytecode.TvmDictPrefixPfxdictaddInst
import org.ton.bytecode.TvmDictPrefixPfxdictconstgetjmpInst
import org.ton.bytecode.TvmDictPrefixPfxdictdelInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetexecInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetjmpInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetqInst
import org.ton.bytecode.TvmDictPrefixPfxdictreplaceInst
import org.ton.bytecode.TvmDictPrefixPfxdictsetInst
import org.ton.bytecode.TvmDictSetBuilderDictaddbInst
import org.ton.bytecode.TvmDictSetBuilderDictaddgetbInst
import org.ton.bytecode.TvmDictSetBuilderDictiaddbInst
import org.ton.bytecode.TvmDictSetBuilderDictiaddgetbInst
import org.ton.bytecode.TvmDictSetBuilderDictireplacebInst
import org.ton.bytecode.TvmDictSetBuilderDictireplacegetbInst
import org.ton.bytecode.TvmDictSetBuilderDictisetbInst
import org.ton.bytecode.TvmDictSetBuilderDictisetgetbInst
import org.ton.bytecode.TvmDictSetBuilderDictreplacebInst
import org.ton.bytecode.TvmDictSetBuilderDictreplacegetbInst
import org.ton.bytecode.TvmDictSetBuilderDictsetbInst
import org.ton.bytecode.TvmDictSetBuilderDictsetgetbInst
import org.ton.bytecode.TvmDictSetBuilderDictuaddbInst
import org.ton.bytecode.TvmDictSetBuilderDictuaddgetbInst
import org.ton.bytecode.TvmDictSetBuilderDictureplacebInst
import org.ton.bytecode.TvmDictSetBuilderDictureplacegetbInst
import org.ton.bytecode.TvmDictSetBuilderDictusetbInst
import org.ton.bytecode.TvmDictSetBuilderDictusetgetbInst
import org.ton.bytecode.TvmDictSetDictaddInst
import org.ton.bytecode.TvmDictSetDictaddgetInst
import org.ton.bytecode.TvmDictSetDictaddgetrefInst
import org.ton.bytecode.TvmDictSetDictaddrefInst
import org.ton.bytecode.TvmDictSetDictiaddInst
import org.ton.bytecode.TvmDictSetDictiaddgetInst
import org.ton.bytecode.TvmDictSetDictiaddgetrefInst
import org.ton.bytecode.TvmDictSetDictiaddrefInst
import org.ton.bytecode.TvmDictSetDictireplaceInst
import org.ton.bytecode.TvmDictSetDictireplacegetInst
import org.ton.bytecode.TvmDictSetDictireplacegetrefInst
import org.ton.bytecode.TvmDictSetDictireplacerefInst
import org.ton.bytecode.TvmDictSetDictisetInst
import org.ton.bytecode.TvmDictSetDictisetgetInst
import org.ton.bytecode.TvmDictSetDictisetgetrefInst
import org.ton.bytecode.TvmDictSetDictisetrefInst
import org.ton.bytecode.TvmDictSetDictreplaceInst
import org.ton.bytecode.TvmDictSetDictreplacegetInst
import org.ton.bytecode.TvmDictSetDictreplacegetrefInst
import org.ton.bytecode.TvmDictSetDictreplacerefInst
import org.ton.bytecode.TvmDictSetDictsetInst
import org.ton.bytecode.TvmDictSetDictsetgetInst
import org.ton.bytecode.TvmDictSetDictsetgetrefInst
import org.ton.bytecode.TvmDictSetDictsetrefInst
import org.ton.bytecode.TvmDictSetDictuaddInst
import org.ton.bytecode.TvmDictSetDictuaddgetInst
import org.ton.bytecode.TvmDictSetDictuaddgetrefInst
import org.ton.bytecode.TvmDictSetDictuaddrefInst
import org.ton.bytecode.TvmDictSetDictureplaceInst
import org.ton.bytecode.TvmDictSetDictureplacegetInst
import org.ton.bytecode.TvmDictSetDictureplacegetrefInst
import org.ton.bytecode.TvmDictSetDictureplacerefInst
import org.ton.bytecode.TvmDictSetDictusetInst
import org.ton.bytecode.TvmDictSetDictusetgetInst
import org.ton.bytecode.TvmDictSetDictusetgetrefInst
import org.ton.bytecode.TvmDictSetDictusetrefInst
import org.ton.bytecode.TvmDictSpecialDictigetexecInst
import org.ton.bytecode.TvmDictSpecialDictigetexeczInst
import org.ton.bytecode.TvmDictSpecialDictigetjmpInst
import org.ton.bytecode.TvmDictSpecialDictigetjmpzInst
import org.ton.bytecode.TvmDictSpecialDictpushconstInst
import org.ton.bytecode.TvmDictSpecialDictugetexecInst
import org.ton.bytecode.TvmDictSpecialDictugetexeczInst
import org.ton.bytecode.TvmDictSpecialDictugetjmpInst
import org.ton.bytecode.TvmDictSpecialDictugetjmpzInst
import org.ton.bytecode.TvmDictSubSubdictgetInst
import org.ton.bytecode.TvmDictSubSubdictigetInst
import org.ton.bytecode.TvmDictSubSubdictirpgetInst
import org.ton.bytecode.TvmDictSubSubdictrpgetInst
import org.ton.bytecode.TvmDictSubSubdictugetInst
import org.ton.bytecode.TvmDictSubSubdicturpgetInst
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
import org.ton.bytecode.TvmStackComplexPu2xcInst
import org.ton.bytecode.TvmStackComplexPush2Inst
import org.ton.bytecode.TvmStackComplexPush3Inst
import org.ton.bytecode.TvmStackComplexPushLongInst
import org.ton.bytecode.TvmStackComplexPuxc2Inst
import org.ton.bytecode.TvmStackComplexPuxcInst
import org.ton.bytecode.TvmStackComplexPuxcpuInst
import org.ton.bytecode.TvmStackComplexReverseInst
import org.ton.bytecode.TvmStackComplexRollxInst
import org.ton.bytecode.TvmStackComplexRotInst
import org.ton.bytecode.TvmStackComplexSwap2Inst
import org.ton.bytecode.TvmStackComplexTuckInst
import org.ton.bytecode.TvmStackComplexXc2puInst
import org.ton.bytecode.TvmStackComplexXchg2Inst
import org.ton.bytecode.TvmStackComplexXchg3AltInst
import org.ton.bytecode.TvmStackComplexXchg3Inst
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

            is TvmDictGetDictgetInst, is TvmDictGetDictgetrefInst,
            is TvmDictGetDictugetInst, is TvmDictGetDictugetrefInst,
            is TvmDictGetDictigetInst, is TvmDictGetDictigetrefInst,
            is TvmDictMayberefDictgetoptrefInst, is TvmDictMayberefDictigetoptrefInst,
            is TvmDictMayberefDictugetoptrefInst,

            is TvmDictNextDictgetnextInst, is TvmDictNextDictgetnexteqInst, is TvmDictNextDictgetprevInst,
            is TvmDictNextDictgetpreveqInst,
            is TvmDictNextDictugetnextInst, is TvmDictNextDictugetnexteqInst, is TvmDictNextDictugetprevInst,
            is TvmDictNextDictugetpreveqInst,
            is TvmDictNextDictigetnextInst, is TvmDictNextDictigetnexteqInst, is TvmDictNextDictigetprevInst,
            is TvmDictNextDictigetpreveqInst,

            is TvmDictSpecialDictugetexecInst, is TvmDictSpecialDictugetexeczInst,
            is TvmDictSpecialDictugetjmpInst, is TvmDictSpecialDictugetjmpzInst,
            is TvmDictSpecialDictigetexecInst, is TvmDictSpecialDictigetexeczInst,
            is TvmDictSpecialDictigetjmpInst, is TvmDictSpecialDictigetjmpzInst,

            is TvmDictSubSubdictgetInst, is TvmDictSubSubdictugetInst, is TvmDictSubSubdictigetInst,
            is TvmDictSubSubdictrpgetInst, is TvmDictSubSubdicturpgetInst, is TvmDictSubSubdictirpgetInst,

            is TvmDictPrefixPfxdictgetInst, is TvmDictPrefixPfxdictgetqInst,
            is TvmDictPrefixPfxdictgetjmpInst, is TvmDictPrefixPfxdictgetexecInst,
            -> DictGetHandler

            is TvmDictSetDictsetInst, is TvmDictSetDictsetrefInst,
            is TvmDictSetDictusetInst, is TvmDictSetDictusetrefInst,
            is TvmDictSetDictisetInst, is TvmDictSetDictisetrefInst,

            is TvmDictSetDictsetgetInst, is TvmDictSetDictsetgetrefInst,
            is TvmDictSetDictusetgetInst, is TvmDictSetDictusetgetrefInst,
            is TvmDictSetDictisetgetInst, is TvmDictSetDictisetgetrefInst,
            is TvmDictMayberefDictsetgetoptrefInst, is TvmDictMayberefDictusetgetoptrefInst,
            is TvmDictMayberefDictisetgetoptrefInst,

            is TvmDictSetDictaddInst, is TvmDictSetDictaddrefInst,
            is TvmDictSetDictuaddInst, is TvmDictSetDictuaddrefInst,
            is TvmDictSetDictiaddInst, is TvmDictSetDictiaddrefInst,
            is TvmDictSetDictreplaceInst, is TvmDictSetDictreplacerefInst,
            is TvmDictSetDictureplaceInst, is TvmDictSetDictureplacerefInst,
            is TvmDictSetDictireplaceInst, is TvmDictSetDictireplacerefInst,

            is TvmDictSetDictaddgetInst, is TvmDictSetDictaddgetrefInst,
            is TvmDictSetDictuaddgetInst, is TvmDictSetDictuaddgetrefInst,
            is TvmDictSetDictiaddgetInst, is TvmDictSetDictiaddgetrefInst,
            is TvmDictSetDictreplacegetInst, is TvmDictSetDictreplacegetrefInst,
            is TvmDictSetDictureplacegetInst, is TvmDictSetDictureplacegetrefInst,
            is TvmDictSetDictireplacegetInst, is TvmDictSetDictireplacegetrefInst,

            is TvmDictSetBuilderDictsetbInst, is TvmDictSetBuilderDictusetbInst,
            is TvmDictSetBuilderDictisetbInst,
            is TvmDictSetBuilderDictsetgetbInst, is TvmDictSetBuilderDictusetgetbInst,
            is TvmDictSetBuilderDictisetgetbInst,
            is TvmDictSetBuilderDictaddbInst, is TvmDictSetBuilderDictuaddbInst, is TvmDictSetBuilderDictiaddbInst,
            is TvmDictSetBuilderDictaddgetbInst, is TvmDictSetBuilderDictuaddgetbInst,
            is TvmDictSetBuilderDictiaddgetbInst,
            is TvmDictSetBuilderDictreplacebInst, is TvmDictSetBuilderDictureplacebInst,
            is TvmDictSetBuilderDictireplacebInst,
            is TvmDictSetBuilderDictreplacegetbInst, is TvmDictSetBuilderDictureplacegetbInst,
            is TvmDictSetBuilderDictireplacegetbInst,

            is TvmDictPrefixPfxdictsetInst, is TvmDictPrefixPfxdictaddInst, is TvmDictPrefixPfxdictreplaceInst,
            -> DictSetHandler

            is TvmDictDeleteDictdelInst, is TvmDictDeleteDictdelgetInst, is TvmDictDeleteDictdelgetrefInst,
            is TvmDictDeleteDictudelInst, is TvmDictDeleteDictudelgetInst, is TvmDictDeleteDictudelgetrefInst,
            is TvmDictDeleteDictidelInst, is TvmDictDeleteDictidelgetInst, is TvmDictDeleteDictidelgetrefInst,
            is TvmDictPrefixPfxdictdelInst,
            -> DictDelHandler

            is TvmDictMinDictminInst, is TvmDictMinDictminrefInst,
            is TvmDictMinDictuminInst, is TvmDictMinDictuminrefInst,
            is TvmDictMinDictiminInst, is TvmDictMinDictiminrefInst,

            is TvmDictMinDictmaxInst, is TvmDictMinDictmaxrefInst,
            is TvmDictMinDictumaxInst, is TvmDictMinDictumaxrefInst,
            is TvmDictMinDictimaxInst, is TvmDictMinDictimaxrefInst,

            is TvmDictMinDictremminInst, is TvmDictMinDictremminrefInst,
            is TvmDictMinDicturemminInst, is TvmDictMinDicturemminrefInst,
            is TvmDictMinDictiremminInst, is TvmDictMinDictiremminrefInst,

            is TvmDictMinDictremmaxInst, is TvmDictMinDictremmaxrefInst,
            is TvmDictMinDicturemmaxInst, is TvmDictMinDicturemmaxrefInst,
            is TvmDictMinDictiremmaxInst, is TvmDictMinDictiremmaxrefInst,
            -> DictMinMaxHandler

            is TvmDictSpecialDictpushconstInst -> DictPushConstHandler

            is TvmDictPrefixPfxdictconstgetjmpInst -> DictConstGetHandler

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
            inputs.add(0, stack.pop(0))
        }

        val key = stack.pop(0)
        val dict = stack.pop(0)
        inputs.add(0, key)
        inputs.add(0, dict)

        val outputs = mutableListOf<TacStackValue>()

        if (inst.stackOutputs == null) {
            val type = if (inst.dictInstHasRef()) TvmSpecType.CELL else TvmSpecType.SLICE
            val valueVar = TacVar("val_${ctx.nextVarId()}", listOf(type))
            outputs.add(valueVar)
        } else {
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
            inputs.add(0, stack.pop(0))
        }

        val value = stack.pop(0)
        val key = stack.pop(0)
        val dict = stack.pop(0)

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
            inputs.add(0, stack.pop(0))
        }

        val key = stack.pop(0)
        val dict = stack.pop(0)

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
            inputs.add(0, stack.pop(0))
        }

        val dict = stack.pop(0)
        inputs.add(0, dict)

        val outputs = mutableListOf<TacStackValue>()
        inst.stackOutputs?.forEach { outputDesc ->
            val rawName = if (outputDesc is TvmSimpleStackEntryDescription) outputDesc.name else "val"
            val newName = "${rawName}_${ctx.nextVarId()}"

            val type = if (inst.dictInstHasRef() && rawName != "k") TvmSpecType.CELL else TvmSpecType.SLICE
            val finalType = if (rawName == "f") listOf(TvmSpecType.INT) else listOf(type)

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

object DictPushConstHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val nVal =
            (inst.operands["n"] as? Number)?.toLong()
                ?: error("DICTPUSHCONST missing 'n' operand")

        val dictName = "D_const_${ctx.nextVarId()}"
        val dictVar = TacVar(dictName, listOf(TvmSpecType.CELL))

        val nName = "n_${ctx.nextVarId()}"
        val nVar = TacIntValue(nName, nVal)

        stack.push(dictVar)
        stack.push(nVar)

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = emptyList(),
                outputs = listOf(dictVar, nVar),
                blocks = emptyList(),
            ),
        )
    }
}

object DictConstGetHandler : TacInstructionHandler {
    override fun <Inst : AbstractTacInst> handle(
        ctx: TacGenerationContext<Inst>,
        stack: Stack,
        inst: TvmRealInst,
        registerState: RegisterState,
    ): List<TacInst> {
        val key = stack.pop(0)

        val outputs = mutableListOf<TacStackValue>()

        inst.stackOutputs?.forEach { outputDesc ->
            val rawName = if (outputDesc is TvmSimpleStackEntryDescription) outputDesc.name else "val"
            val newName = "${rawName}_${ctx.nextVarId()}"

            val finalType = if (rawName == "f") listOf(TvmSpecType.INT) else listOf(TvmSpecType.SLICE)

            outputs.add(TacVar(newName, finalType))
        }

        outputs.forEach { stack.push(it) }

        return listOf(
            TacOrdinaryInst<AbstractTacInst>(
                mnemonic = inst.mnemonic,
                operands = inst.operands,
                inputs = listOf(key),
                outputs = outputs,
                blocks = emptyList(),
            ),
        )
    }
}
