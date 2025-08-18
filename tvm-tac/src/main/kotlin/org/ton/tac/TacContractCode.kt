package org.ton.tac

import org.ton.bytecode.MethodId

data class TacContractCode<out Inst>(
    val mainMethod: TacMainMethod<Inst>,
    val methods: Map<MethodId, TacMethod<Inst>>,
)
