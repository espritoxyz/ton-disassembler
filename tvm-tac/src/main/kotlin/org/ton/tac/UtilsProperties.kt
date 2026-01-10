package org.ton.tac

import org.ton.bytecode.TvmContConditionalIfInst
import org.ton.bytecode.TvmContConditionalIfelseInst
import org.ton.bytecode.TvmContConditionalIfjmpInst
import org.ton.bytecode.TvmContConditionalIfjmprefInst
import org.ton.bytecode.TvmContConditionalIfnotInst
import org.ton.bytecode.TvmContConditionalIfnotjmpInst
import org.ton.bytecode.TvmContConditionalIfnotjmprefInst
import org.ton.bytecode.TvmContConditionalIfnotrefInst
import org.ton.bytecode.TvmContConditionalIfrefInst
import org.ton.bytecode.TvmContConditionalIfrefelseInst
import org.ton.bytecode.TvmContConditionalIfrefelserefInst
import org.ton.bytecode.TvmDictDeleteDictdelInst
import org.ton.bytecode.TvmDictDeleteDictdelgetInst
import org.ton.bytecode.TvmDictDeleteDictdelgetrefInst
import org.ton.bytecode.TvmDictDeleteDictidelInst
import org.ton.bytecode.TvmDictDeleteDictidelgetInst
import org.ton.bytecode.TvmDictDeleteDictidelgetrefInst
import org.ton.bytecode.TvmDictDeleteDictudelInst
import org.ton.bytecode.TvmDictDeleteDictudelgetInst
import org.ton.bytecode.TvmDictDeleteDictudelgetrefInst
import org.ton.bytecode.TvmDictGetInst
import org.ton.bytecode.TvmDictMinInst
import org.ton.bytecode.TvmDictPrefixPfxdictaddInst
import org.ton.bytecode.TvmDictPrefixPfxdictdelInst
import org.ton.bytecode.TvmDictPrefixPfxdictgetqInst
import org.ton.bytecode.TvmDictPrefixPfxdictreplaceInst
import org.ton.bytecode.TvmDictPrefixPfxdictsetInst
import org.ton.bytecode.TvmExceptionsThrowifInst
import org.ton.bytecode.TvmExceptionsThrowifShortInst
import org.ton.bytecode.TvmExceptionsThrowifnotInst
import org.ton.bytecode.TvmExceptionsThrowifnotShortInst
import org.ton.bytecode.TvmRealInst
import org.ton.bytecode.TvmTupleNullswapifInst
import org.ton.bytecode.TvmTupleNullswapifnotInst

// TODO(now it is full only for dictionaries because now it is necessary only for them. Complete it for all instructions)
fun TvmRealInst.hasVariableOutputs(): Boolean =
    when (this) {
        is TvmDictGetInst -> true
        is TvmDictMinInst -> true
        is TvmDictPrefixPfxdictaddInst -> true
        is TvmDictPrefixPfxdictdelInst -> true
        is TvmDictPrefixPfxdictgetqInst -> true
        is TvmDictPrefixPfxdictreplaceInst -> true
        is TvmDictPrefixPfxdictsetInst -> true
        is TvmDictDeleteDictdelInst -> true
        is TvmDictDeleteDictdelgetInst -> true
        is TvmDictDeleteDictdelgetrefInst -> true
        is TvmDictDeleteDictidelInst -> true
        is TvmDictDeleteDictidelgetInst -> true
        is TvmDictDeleteDictidelgetrefInst -> true
        is TvmDictDeleteDictudelInst -> true
        is TvmDictDeleteDictudelgetInst -> true
        is TvmDictDeleteDictudelgetrefInst -> true

        else -> false
    }

fun TvmRealInst.allowedAfterInstructionWithVariableOutputs(): Boolean =
    when (this) {
        is TvmTupleNullswapifnotInst, is TvmTupleNullswapifInst -> true

        is TvmContConditionalIfInst, is TvmContConditionalIfnotInst,
        is TvmContConditionalIfjmpInst, is TvmContConditionalIfnotjmpInst,
        is TvmContConditionalIfrefInst, is TvmContConditionalIfnotrefInst,
        is TvmContConditionalIfjmprefInst, is TvmContConditionalIfnotjmprefInst,
        is TvmContConditionalIfelseInst, is TvmContConditionalIfrefelseInst,
        is TvmContConditionalIfrefelserefInst,
        -> true

        is TvmExceptionsThrowifInst, is TvmExceptionsThrowifnotInst,
        is TvmExceptionsThrowifShortInst, is TvmExceptionsThrowifnotShortInst,
        -> true

        else -> false
    }
