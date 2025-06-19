package org.ton.tac

data class TacContractCode(
    val mainMethod: TacMainMethod,
    val methods: Map<MethodId, TacMethod>,
    val inlineMethods: Map<String, TacInlineMethod>
)
