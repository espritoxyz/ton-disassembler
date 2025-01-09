package org.ton.bytecode

import kotlinx.serialization.Serializable

@Serializable
sealed interface TvmInst {
    val mnemonic: String
    val location: TvmInstLocation
    val gasConsumption: TvmGas
    // TODO should we define opcodes?
}
