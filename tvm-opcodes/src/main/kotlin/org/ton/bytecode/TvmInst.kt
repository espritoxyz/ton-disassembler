package org.ton.bytecode

import kotlinx.serialization.Serializable

@Serializable
sealed interface TvmInst {
    val mnemonic: String
    val location: TvmInstLocation
    val gasConsumption: TvmGas
}

/**
 * These instructions never occur in disassembler output.
 * They are needed for custom modifications of [TvmContractCode].
 * */
interface TvmArtificialInst : TvmInst {
    override val gasConsumption: TvmGas
        get() = TvmFixedGas(value = 0)

    fun checkLocationInitialized() {
        val lambdaLoc = location as? TvmInstLambdaLocation
        // this will fail if parent is not initialized
        lambdaLoc?.parent
    }
}
