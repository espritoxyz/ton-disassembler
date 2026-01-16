package org.ton.bytecode

import org.ton.tac.DefaultSpecHandler
import org.ton.tac.DictDelHandler
import org.ton.tac.DictGetHandler
import org.ton.tac.DictMinMaxHandler
import org.ton.tac.DictSetHandler
import org.ton.tac.TacInstructionHandler

fun TvmRealInst.dictInstHasIntegerKey(): Boolean =
    when (this) {
        is TvmDictGetDictugetInst, is TvmDictGetDictugetrefInst,
        is TvmDictGetDictigetInst, is TvmDictGetDictigetrefInst,
        is TvmDictMayberefDictugetoptrefInst, is TvmDictMayberefDictigetoptrefInst,

        is TvmDictNextDictugetnextInst, is TvmDictNextDictugetnexteqInst,
        is TvmDictNextDictugetprevInst, is TvmDictNextDictugetpreveqInst,
        is TvmDictNextDictigetnextInst, is TvmDictNextDictigetnexteqInst,
        is TvmDictNextDictigetprevInst, is TvmDictNextDictigetpreveqInst,

        is TvmDictSpecialDictugetexecInst, is TvmDictSpecialDictugetexeczInst,
        is TvmDictSpecialDictugetjmpInst, is TvmDictSpecialDictugetjmpzInst,
        is TvmDictSpecialDictigetexecInst, is TvmDictSpecialDictigetexeczInst,
        is TvmDictSpecialDictigetjmpInst, is TvmDictSpecialDictigetjmpzInst,
        is TvmDictSubSubdictugetInst, is TvmDictSubSubdicturpgetInst,
        TvmDictSubSubdictigetInst, is TvmDictSubSubdictirpgetInst,

        is TvmDictSetDictusetInst, is TvmDictSetDictusetrefInst,
        is TvmDictSetDictisetInst, is TvmDictSetDictisetrefInst,
        is TvmDictSetDictusetgetInst, is TvmDictSetDictusetgetrefInst,
        is TvmDictSetDictisetgetInst, is TvmDictSetDictisetgetrefInst,

        is TvmDictMayberefDictusetgetoptrefInst, is TvmDictMayberefDictisetgetoptrefInst,
        is TvmDictSetDictuaddInst, is TvmDictSetDictuaddrefInst,
        is TvmDictSetDictuaddgetInst, is TvmDictSetDictuaddgetrefInst,
        is TvmDictSetDictiaddInst, is TvmDictSetDictiaddrefInst,
        is TvmDictSetDictiaddgetInst, is TvmDictSetDictiaddgetrefInst,
        is TvmDictSetDictureplaceInst, is TvmDictSetDictureplacerefInst,
        is TvmDictSetDictureplacegetInst, is TvmDictSetDictureplacegetrefInst,
        is TvmDictSetDictireplaceInst, is TvmDictSetDictireplacerefInst,
        is TvmDictSetDictireplacegetInst, is TvmDictSetDictireplacegetrefInst,

        is TvmDictSetBuilderDictusetbInst, is TvmDictSetBuilderDictusetgetbInst,
        is TvmDictSetBuilderDictisetbInst, is TvmDictSetBuilderDictisetgetbInst,
        is TvmDictSetBuilderDictuaddbInst, is TvmDictSetBuilderDictuaddgetbInst,
        is TvmDictSetBuilderDictiaddbInst, is TvmDictSetBuilderDictiaddgetbInst,
        is TvmDictSetBuilderDictureplacebInst, is TvmDictSetBuilderDictureplacegetbInst,
        is TvmDictSetBuilderDictireplacebInst, is TvmDictSetBuilderDictireplacegetbInst,

        is TvmDictDeleteDictudelInst, is TvmDictDeleteDictudelgetInst, is TvmDictDeleteDictudelgetrefInst,
        is TvmDictDeleteDictidelInst, is TvmDictDeleteDictidelgetInst, is TvmDictDeleteDictidelgetrefInst,

        is TvmDictMinDictuminInst, is TvmDictMinDictuminrefInst,
        is TvmDictMinDictiminInst, is TvmDictMinDictiminrefInst,
        is TvmDictMinDictumaxInst, is TvmDictMinDictumaxrefInst,
        is TvmDictMinDictimaxInst, is TvmDictMinDictimaxrefInst,
        is TvmDictMinDicturemminInst, is TvmDictMinDicturemminrefInst,
        is TvmDictMinDictiremminInst, is TvmDictMinDictiremminrefInst,
        is TvmDictMinDicturemmaxInst, is TvmDictMinDicturemmaxrefInst,
        is TvmDictMinDictiremmaxInst, is TvmDictMinDictiremmaxrefInst,
        -> true

        else -> false
    }

fun TvmRealInst.dictInstHasRef(): Boolean =
    when (this) {
        is TvmDictGetDictgetrefInst, is TvmDictGetDictigetrefInst, is TvmDictGetDictugetrefInst,
        is TvmDictMayberefDictgetoptrefInst, is TvmDictMayberefDictigetoptrefInst, is TvmDictMayberefDictugetoptrefInst,

        is TvmDictSubSubdictgetInst, is TvmDictSubSubdictugetInst, is TvmDictSubSubdictigetInst,
        is TvmDictSubSubdictrpgetInst, is TvmDictSubSubdicturpgetInst, is TvmDictSubSubdictirpgetInst,

        is TvmDictSetDictsetrefInst, is TvmDictSetDictisetrefInst, is TvmDictSetDictusetrefInst,
        is TvmDictSetDictsetgetrefInst, is TvmDictSetDictisetgetrefInst, is TvmDictSetDictusetgetrefInst,
        is TvmDictSetDictreplacerefInst, is TvmDictSetDictireplacerefInst, is TvmDictSetDictureplacerefInst,
        is TvmDictSetDictreplacegetrefInst, is TvmDictSetDictireplacegetrefInst, is TvmDictSetDictureplacegetrefInst,
        is TvmDictSetDictaddrefInst, is TvmDictSetDictiaddrefInst, is TvmDictSetDictuaddrefInst,
        is TvmDictSetDictaddgetrefInst, is TvmDictSetDictiaddgetrefInst, is TvmDictSetDictuaddgetrefInst,
        is TvmDictMayberefDictsetgetoptrefInst, is TvmDictMayberefDictisetgetoptrefInst,
        is TvmDictMayberefDictusetgetoptrefInst,

        is TvmDictDeleteDictdelgetrefInst, is TvmDictDeleteDictidelgetrefInst, is TvmDictDeleteDictudelgetrefInst,

        is TvmDictMinDictminrefInst, is TvmDictMinDictiminrefInst, is TvmDictMinDictuminrefInst,
        is TvmDictMinDictmaxrefInst, is TvmDictMinDictimaxrefInst, is TvmDictMinDictumaxrefInst,
        is TvmDictMinDictremminrefInst, is TvmDictMinDictiremminrefInst, is TvmDictMinDicturemminrefInst,
        is TvmDictMinDictremmaxrefInst, is TvmDictMinDictiremmaxrefInst, is TvmDictMinDicturemmaxrefInst,

        is TvmDictSpecialDictpushconstInst,
        -> true

        else -> false
    }

fun getDictHandler(inst: TvmRealInst): TacInstructionHandler =
    when (inst) {
        is TvmDictGetDictgetInst, is TvmDictGetDictgetrefInst,
        is TvmDictGetDictugetInst, is TvmDictGetDictugetrefInst,
        is TvmDictGetDictigetInst, is TvmDictGetDictigetrefInst,

        is TvmDictNextDictgetnextInst, is TvmDictNextDictgetnexteqInst, is TvmDictNextDictgetprevInst,
        is TvmDictNextDictgetpreveqInst,
        is TvmDictNextDictugetnextInst, is TvmDictNextDictugetnexteqInst, is TvmDictNextDictugetprevInst,
        is TvmDictNextDictugetpreveqInst,
        is TvmDictNextDictigetnextInst, is TvmDictNextDictigetnexteqInst, is TvmDictNextDictigetprevInst,
        is TvmDictNextDictigetpreveqInst,

        is TvmDictPrefixPfxdictgetqInst,
        -> DictGetHandler

        is TvmDictSetDictsetgetInst, is TvmDictSetDictsetgetrefInst,
        is TvmDictSetDictusetgetInst, is TvmDictSetDictusetgetrefInst,
        is TvmDictSetDictisetgetInst, is TvmDictSetDictisetgetrefInst,

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

        is TvmDictDeleteDictdelgetInst, is TvmDictDeleteDictdelgetrefInst,
        is TvmDictDeleteDictudelgetInst, is TvmDictDeleteDictudelgetrefInst,
        is TvmDictDeleteDictidelgetInst, is TvmDictDeleteDictidelgetrefInst,
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

        else -> DefaultSpecHandler
    }

val allowedConditionalPairsMap =
    mapOf(
        TvmDictGetDictigetrefInst::class to TvmTupleNullswapifnotInst::class,
        TvmDictGetDictugetrefInst::class to TvmTupleNullswapifnotInst::class,
        TvmDictGetDictigetInst::class to TvmTupleNullswapifnotInst::class,
        TvmDictGetDictugetInst::class to TvmTupleNullswapifnotInst::class,
        TvmDictDeleteDictidelgetInst::class to TvmTupleNullswapifnotInst::class,
        TvmDictDeleteDictudelgetInst::class to TvmTupleNullswapifnotInst::class,
        TvmDictMinDicturemminInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictiremminInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictremminInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDicturemmaxInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictiremmaxInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictremmaxInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictuminInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictumaxInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictuminrefInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictumaxrefInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictiminInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictimaxInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictiminrefInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictMinDictimaxrefInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictugetnextInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictugetnexteqInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictugetprevInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictugetpreveqInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictigetnextInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictigetnexteqInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictigetprevInst::class to TvmTupleNullswapifnot2Inst::class,
        TvmDictNextDictigetpreveqInst::class to TvmTupleNullswapifnot2Inst::class,
    )
