package org.ton.disasm.utils

import java.math.BigInteger

fun String.binaryStringToSignedBigInteger(): BigInteger =
    if (startsWith('0')) {
        // positive integer
        BigInteger(this, 2)
    } else {
        // negative integer
        BigInteger(this, 2) - BigInteger.TWO.pow(length)
    }