// Generated
package org.ton.bytecode
import org.ton.disasm.TvmPhysicalInstLocation

val tvmDefaultInstructions = mapOf(
    "stack_basic" to listOf(
        TvmStackBasicNopInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackBasicXchg0iInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackBasicXchgIjInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackBasicXchg0iLongInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackBasicXchg1iInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackBasicPushInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackBasicPopInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
    ),
    "stack_complex" to listOf(
        TvmStackComplexXchg3Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexXchg2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexXcpuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexPuxcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexPush2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexXchg3AltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexXc2puInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexXcpuxcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexXcpu2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexPuxc2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexPuxcpuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexPu2xcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexPush3Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
        TvmStackComplexBlkswapInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexPushLongInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackComplexPopLongInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackComplexRotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexRotrevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexSwap2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexDrop2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexDup2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexOver2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexReverseInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexBlkdropInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmStackComplexBlkpushInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmStackComplexPickInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexRollxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexMinusrollxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexBlkswxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexRevxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexDropxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexTuckInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexXchgxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexDepthInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexChkdepthInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexOnlytopxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexOnlyxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmStackComplexBlkdrop2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
    ),
    "tuple" to listOf(
        TvmTupleNullInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleIsnullInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleTupleInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleIndexInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleUntupleInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleUnpackfirstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleExplodeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleSetindexInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleIndexqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleSetindexqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmTupleTuplevarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleIndexvarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleUntuplevarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleUnpackfirstvarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleExplodevarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleSetindexvarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleIndexvarqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleSetindexvarqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleTlenInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleQtlenInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleIstupleInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleLastInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleTpushInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleTpopInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullswapifInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullswapifnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullrotrifInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullrotrifnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullswapif2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullswapifnot2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullrotrif2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleNullrotrifnot2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmTupleIndex2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmTupleIndex3Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
            0,
        ),
    ),
    "const_int" to listOf(
        TvmConstIntPushint4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmConstIntPushint8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmConstIntPushint16Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmConstIntPushintLongInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            "0",
        ),
        TvmConstIntPushpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmConstIntPushnanInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmConstIntPushpow2decInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmConstIntPushnegpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
    ),
    "const_data" to listOf(
        TvmConstDataPushrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmConstDataPushrefsliceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmConstDataPushrefcontInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmConstDataPushsliceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmConstDataPushsliceRefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmConstDataPushsliceLongInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmConstDataPushcontInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmConstDataPushcontShortInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
    ),
    "arithm_basic" to listOf(
        TvmArithmBasicAddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmBasicSubInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmBasicSubrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmBasicNegateInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmBasicIncInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmBasicDecInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmBasicAddconstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmBasicMulconstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmBasicMulInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "arithm_div" to listOf(
        TvmArithmDivAdddivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivAdddivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivAdddivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivDivInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivDivrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivDivcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivModInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivModrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivModcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivDivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivDivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivDivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivAddrshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivAddrshiftmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivAddrshiftmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivRshiftrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivRshiftcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivModpow2VarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivModpow2rVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivModpow2cVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivRshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivRshiftmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivRshiftmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivAddrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivAddrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivAddrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivRshiftrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivRshiftcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivModpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivModpow2rInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivModpow2cInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivRshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivRshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivRshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMuladddivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuladddivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuladddivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuldivInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuldivrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuldivcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuldivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuldivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuldivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMuladdrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMuladdrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMuladdrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulrshiftVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulrshiftrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulrshiftcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulmodpow2VarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulmodpow2rVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulmodpow2cVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulrshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulrshiftrmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulrshiftcmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivMulrshiftInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulrshiftrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulrshiftcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulmodpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulmodpow2rInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulmodpow2cInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivMulrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftadddivmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftadddivmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftadddivmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftdivVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftdivrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftdivcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftdivmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftdivmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftdivmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmDivLshiftadddivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftadddivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftadddivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftdivInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftdivrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftdivcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftdivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftdivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmDivLshiftdivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
    ),
    "arithm_logical" to listOf(
        TvmArithmLogicalLshiftInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmLogicalRshiftInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmLogicalLshiftVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalRshiftVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalPow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalAndInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalOrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalXorInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalNotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalFitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmLogicalUfitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmLogicalFitsxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalUfitsxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalBitsizeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalUbitsizeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalMinInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalMaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalMinmaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmLogicalAbsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "arithm_quiet" to listOf(
        TvmArithmQuietQaddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQsubInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQsubrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQnegateInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQincInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdecInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQadddivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQadddivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQadddivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdivInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdivrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdivcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQdivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQaddrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQaddrshiftmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQaddrshiftmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQrshiftrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQrshiftcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmodpow2VarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmodpow2rVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmodpow2cVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQrshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQrshiftmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQrshiftmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQaddrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQaddrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQrshiftrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQrshiftcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmodpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmodpow2rInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmodpow2cInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmuladddivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuladddivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuladddivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuldivInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuldivrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuldivcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuldivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuldivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuldivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmuladdrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmuladdrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmuladdrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulrshiftVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulmodpow2VarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulmodpow2rVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulmodpow2cVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftrmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftcmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulrshiftrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulrshiftcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulmodpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulmodpow2rInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulmodpow2cInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQmulrshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftrmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQmulrshiftcmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftadddivmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftadddivmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftadddivmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftdivVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftdivrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftdivcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftdivmodVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftdivmodrVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftdivmodcVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQlshiftadddivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftadddivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftadddivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftdivInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftdivrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftdivcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftdivmodInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftdivmodrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftdivmodcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQrshiftInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQlshiftVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQrshiftVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQpow2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQandInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQorInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQxorInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQfitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQufitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmArithmQuietQfitsxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmArithmQuietQufitsxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "compare_int" to listOf(
        TvmCompareIntSgnInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntLessInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntEqualInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntLeqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntGreaterInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntNeqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntGeqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntCmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntEqintInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCompareIntLessintInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCompareIntGtintInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCompareIntNeqintInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCompareIntIsnanInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareIntChknanInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "compare_other" to listOf(
        TvmCompareOtherSemptyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdemptyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSremptyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdfirstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdlexcmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdeqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdpfxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdpfxrevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdppfxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdppfxrevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdsfxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdsfxrevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdpsfxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdpsfxrevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdcntlead0Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdcntlead1Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdcnttrail0Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCompareOtherSdcnttrail1Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "cell_build" to listOf(
        TvmCellBuildNewcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildEndcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStiInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrefrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStsliceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStixInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStuxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStixrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStuxrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStixqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStuxqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStixrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStuxrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStiAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStuAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStirInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildSturInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStiqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStuqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStirqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildSturqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildStrefAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStsliceAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStrefrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrefrAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStslicerInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStrefqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrefqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStsliceqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStrefrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrefrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStslicerqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStbrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStrefconstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmCellBuildStref2constInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmCellBuildEndxcInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStile4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStule4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStile8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStule8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBdepthInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBbitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBbitrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBrembitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBremrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBrembitrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBchkbitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildBchkbitsVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBchkrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBchkbitrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBchkbitsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellBuildBchkbitsqVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBchkrefsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildBchkbitrefsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStzeroesInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStonesInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStsameInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellBuildStsliceconstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
    ),
    "cell_parse" to listOf(
        TvmCellParseCtosInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseEndsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdiInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLduInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLdrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdrefrtosInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdsliceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLdixInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLduxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldixInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePlduxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdixqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLduxqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldixqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePlduxqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdiAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLduAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePldiInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePlduInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLdiqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLduqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePldiqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePlduqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePlduzInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLdslicexInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldslicexInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdslicexqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldslicexqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdsliceAltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePldsliceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLdsliceqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParsePldsliceqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseSdcutfirstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdskipfirstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdcutlastInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdskiplastInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdsubstrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdbeginsxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdbeginsxqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdbeginsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmCellParseSdbeginsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmCellParseScutfirstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSskipfirstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseScutlastInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSskiplastInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSubsliceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSplitInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSplitqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseXctosInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseXloadInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseXloadqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSchkbitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSchkrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSchkbitrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSchkbitsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSchkrefsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSchkbitrefsqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldrefvarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSbitsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSbitrefsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldrefidxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseLdile4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdule4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdile8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdule8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldile4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldule4Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldile8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldule8Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdile4qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdule4qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdile8qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdule8qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldile4qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldule4qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldile8qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParsePldule8qInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdzeroesInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdonesInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseLdsameInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseSdepthInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseCdepthInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseClevelInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseClevelmaskInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseChashiInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseCdepthiInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCellParseChashixInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmCellParseCdepthixInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "cont_basic" to listOf(
        TvmContBasicExecuteInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicJmpxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicCallxargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmContBasicCallxargsVarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContBasicJmpxargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContBasicRetargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContBasicRetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicRetaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicBranchInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicCallccInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicJmpxdataInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicCallccargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmContBasicCallxvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicRetvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicJmpxvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicCallccvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicCallrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContBasicJmprefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContBasicJmprefdataInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContBasicRetdataInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContBasicRunvmInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContBasicRunvmxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "cont_conditional" to listOf(
        TvmContConditionalIfretInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfnotretInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfnotjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfelseInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContConditionalIfnotrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContConditionalIfjmprefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContConditionalIfnotjmprefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContConditionalCondselInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalCondselchkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfretaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfnotretaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContConditionalIfrefelseInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContConditionalIfelserefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
        ),
        TvmContConditionalIfrefelserefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmInstList.empty,
            TvmInstList.empty,
        ),
        TvmContConditionalIfbitjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContConditionalIfnbitjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContConditionalIfbitjmprefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            TvmCell(TvmCellData(""), emptyList()),
        ),
        TvmContConditionalIfnbitjmprefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            TvmCell(TvmCellData(""), emptyList()),
        ),
    ),
    "cont_loops" to listOf(
        TvmContLoopsRepeatInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsRepeatendInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsUntilInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsUntilendInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsWhileInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsWhileendInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsAgainInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsAgainendInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsRepeatbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsRepeatendbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsUntilbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsUntilendbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsWhilebrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsWhileendbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsAgainbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContLoopsAgainendbrkInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "cont_stack" to listOf(
        TvmContStackSetcontargsNInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmContStackReturnargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContStackReturnvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContStackSetcontvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContStackSetnumvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "cont_create" to listOf(
        TvmContCreateBlessInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContCreateBlessvarargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContCreateBlessargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
    ),
    "cont_registers" to listOf(
        TvmContRegistersPushctrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersPopctrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersSetcontctrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersSetretctrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersSetaltctrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersPopsaveInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersSaveInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersSavealtInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersSavebothInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContRegistersPushctrxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersPopctrxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersSetcontctrxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersComposInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersComposaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersComposbothInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersAtexitInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersAtexitaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersSetexitaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersThenretInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersThenretaltInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersInvertInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersBoolevalInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersSamealtInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmContRegistersSamealtsaveInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "cont_dict" to listOf(
        TvmContDictCalldictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContDictCalldictLongInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContDictJmpdictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmContDictPreparedictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
    ),
    "exceptions" to listOf(
        TvmExceptionsThrowShortInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowifShortInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowifnotShortInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowargInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowifInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowargifInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowifnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowargifnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmExceptionsThrowanyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsThrowarganyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsThrowanyifInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsThrowarganyifInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsThrowanyifnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsThrowarganyifnotInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsTryInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmExceptionsTryargsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
    ),
    "dict_serial" to listOf(
        TvmDictSerialStdictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialSkipdictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialLddictsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialPlddictsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialLddictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialPlddictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialLddictqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSerialPlddictqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_get" to listOf(
        TvmDictGetDictgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictGetDictgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictGetDictigetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictGetDictigetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictGetDictugetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictGetDictugetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_set" to listOf(
        TvmDictSetDictsetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictsetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictisetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictisetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictusetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictusetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictsetgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictsetgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictisetgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictisetgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictusetgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictusetgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictreplaceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictreplacerefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictireplaceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictireplacerefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictureplaceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictureplacerefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictreplacegetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictreplacegetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictireplacegetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictireplacegetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictureplacegetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictureplacegetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictaddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictaddrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictiaddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictiaddrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictuaddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictuaddrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictaddgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictaddgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictiaddgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictiaddgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictuaddgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetDictuaddgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_set_builder" to listOf(
        TvmDictSetBuilderDictsetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictisetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictusetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictsetgetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictisetgetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictusetgetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictreplacebInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictireplacebInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictureplacebInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictreplacegetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictireplacegetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictureplacegetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictaddbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictiaddbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictuaddbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictaddgetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictiaddgetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSetBuilderDictuaddgetbInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_delete" to listOf(
        TvmDictDeleteDictdelInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictidelInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictudelInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictdelgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictdelgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictidelgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictidelgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictudelgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictDeleteDictudelgetrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_mayberef" to listOf(
        TvmDictMayberefDictgetoptrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMayberefDictigetoptrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMayberefDictugetoptrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMayberefDictsetgetoptrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMayberefDictisetgetoptrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMayberefDictusetgetoptrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_prefix" to listOf(
        TvmDictPrefixPfxdictsetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictreplaceInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictaddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictdelInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictgetqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictgetjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictgetexecInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictPrefixPfxdictconstgetjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
            0,
        ),
    ),
    "dict_next" to listOf(
        TvmDictNextDictgetnextInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictgetnexteqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictgetprevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictgetpreveqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictigetnextInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictigetnexteqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictigetprevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictigetpreveqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictugetnextInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictugetnexteqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictugetprevInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictNextDictugetpreveqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_min" to listOf(
        TvmDictMinDictminInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictminrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictiminInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictiminrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictuminInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictuminrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictmaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictmaxrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictimaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictimaxrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictumaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictumaxrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictremminInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictremminrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictiremminInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictiremminrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDicturemminInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDicturemminrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictremmaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictremmaxrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictiremmaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDictiremmaxrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDicturemmaxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictMinDicturemmaxrefInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_special" to listOf(
        TvmDictSpecialDictigetjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictugetjmpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictigetexecInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictugetexecInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictpushconstInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
            0,
        ),
        TvmDictSpecialDictigetjmpzInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictugetjmpzInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictigetexeczInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSpecialDictugetexeczInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "dict_sub" to listOf(
        TvmDictSubSubdictgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSubSubdictigetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSubSubdictugetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSubSubdictrpgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSubSubdictirpgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmDictSubSubdicturpgetInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_gas" to listOf(
        TvmAppGasAcceptInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppGasSetgaslimitInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppGasGasconsumedInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppGasCommitInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_rnd" to listOf(
        TvmAppRndRandu256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppRndRandInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppRndSetrandInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppRndAddrandInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_config" to listOf(
        TvmAppConfigGetparamInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmAppConfigConfigdictInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigConfigparamInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigConfigoptparamInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigPrevmcblocksInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigPrevkeyblockInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGlobalidInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetgasfeeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetstoragefeeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetforwardfeeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetprecompiledgasInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetoriginalfwdfeeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetgasfeesimpleInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppConfigGetforwardfeesimpleInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_global" to listOf(
        TvmAppGlobalGetglobvarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppGlobalGetglobInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmAppGlobalSetglobvarInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppGlobalSetglobInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
    ),
    "app_crypto" to listOf(
        TvmAppCryptoHashcuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashsuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoSha256uInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextSha256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextSha512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextBlake2bInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextKeccak256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextKeccak512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextrSha256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextrSha512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextrBlake2bInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextrKeccak256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextrKeccak512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextaSha256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextaSha512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextaBlake2bInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextaKeccak256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextaKeccak512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextarSha256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextarSha512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextarBlake2bInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextarKeccak256Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoHashextarKeccak512Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoChksignuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoChksignsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoEcrecoverInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoP256ChksignuInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoP256ChksignsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255FromhashInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255ValidateInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255AddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255SubInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255MulInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255MulbaseInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255PushlInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255QvalidateInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255QaddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255QsubInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255QmulInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoRist255QmulbaseInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsVerifyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsAggregateInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsFastaggregateverifyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsAggregateverifyInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1AddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1SubInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1NegInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1MulInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1MultiexpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1ZeroInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsMapToG1Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1IngroupInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG1IszeroInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2AddInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2SubInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2NegInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2MulInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2MultiexpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2ZeroInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsMapToG2Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2IngroupInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsG2IszeroInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsPairingInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCryptoBlsPushrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_misc" to listOf(
        TvmAppMiscCdatasizeqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppMiscCdatasizeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppMiscSdatasizeqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppMiscSdatasizeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_currency" to listOf(
        TvmAppCurrencyLdgramsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyLdvarint16Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyStgramsInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyStvarint16Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyLdvaruint32Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyLdvarint32Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyStvaruint32Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppCurrencyStvarint32Inst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_addr" to listOf(
        TvmAppAddrLdmsgaddrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrLdmsgaddrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrParsemsgaddrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrParsemsgaddrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrRewritestdaddrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrRewritestdaddrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrRewritevaraddrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppAddrRewritevaraddrqInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "app_actions" to listOf(
        TvmAppActionsSendrawmsgInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppActionsRawreserveInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppActionsRawreservexInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppActionsSetcodeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppActionsSetlibcodeInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppActionsChangelibInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
        TvmAppActionsSendmsgInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
    "debug" to listOf(
        TvmDebugDebugInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
            0,
        ),
        TvmDebugDebugstrInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            TvmCell(TvmCellData(""), emptyList()),
        ),
    ),
    "codepage" to listOf(
        TvmCodepageSetcpInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCodepageSetcpSpecialInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
            0,
        ),
        TvmCodepageSetcpxInst(
            TvmMainMethodLocation(0),
            TvmPhysicalInstLocation("", 0),
        ),
    ),
)
