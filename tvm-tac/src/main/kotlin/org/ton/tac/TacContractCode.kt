package org.ton.tac

import org.ton.bytecode.MethodId

data class TacContractCode<Inst>(
    val mainMethod: TacMainMethod<Inst>,
    val methods: Map<MethodId, TacMethod<Inst>>,
    val isolatedContinuations: Map<Int, TacInlineMethod<Inst>>
)
