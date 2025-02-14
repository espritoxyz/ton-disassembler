package org.ton.disasm.utils

import java.math.BigInteger
import org.ton.bigint.BigInt

const val CELL_TYPE_BITS: Int = 8
val ORDINARY_CELL_TYPE: BigInt = BigInt.ONE.negate()

fun String.binaryStringToSignedBigInteger(): BigInteger =
    if (startsWith('0')) {
        // positive integer
        BigInteger(this, 2)
    } else {
        // negative integer
        BigInteger(this, 2) - BigInteger.TWO.pow(length)
    }
