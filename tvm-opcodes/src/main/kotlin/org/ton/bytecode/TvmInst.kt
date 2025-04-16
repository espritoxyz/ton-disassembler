package org.ton.bytecode

import kotlinx.serialization.Serializable
import org.ton.disasm.TvmPhysicalInstLocation

@Serializable
sealed interface TvmInst {
    val mnemonic: String
    val location: TvmInstLocation
    val gasConsumption: TvmGas
    val stackInputs: List<TvmStackEntryDescription>?
    val stackOutputs: List<TvmStackEntryDescription>?
    val branches: List<TvmControlFlowContinuation>
    val noBranch: Boolean
}

@Serializable
sealed interface TvmRealInst : TvmInst {
    val physicalLocation: TvmPhysicalInstLocation
}

@Serializable
sealed interface TvmRealInst : TvmInst {
    val physicalLocation: TvmPhysicalInstLocation
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
