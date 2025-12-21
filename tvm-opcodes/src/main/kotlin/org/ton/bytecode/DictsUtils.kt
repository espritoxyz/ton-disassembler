package org.ton.bytecode

fun TvmRealInst.dictInstHasIntegerKey(): Boolean {
    return when (this) {
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

            // SubDict
        is TvmDictSubSubdictugetInst, is TvmDictSubSubdicturpgetInst,
        is TvmDictSubSubdictigetInst, is TvmDictSubSubdictirpgetInst,

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
        is TvmDictMinDictiremmaxInst, is TvmDictMinDictiremmaxrefInst
            -> true

        else -> false
    }
}

fun TvmRealInst.dictInstHasRef(): Boolean {
    return when (this) {
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

        is TvmDictSpecialDictpushconstInst
            -> true

        else -> false
    }
}
