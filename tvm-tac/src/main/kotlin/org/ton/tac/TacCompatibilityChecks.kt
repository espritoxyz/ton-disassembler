package org.ton.tac

import org.ton.bytecode.TvmSpecType

fun tupleCompatible(
    val1: TacTupleValue,
    val2: TacStackValue,
): Boolean {
    val tuple2 = val2 as TacTupleValue
    if (val1.elements.size != tuple2.elements.size) return false

    for (i in val1.elements.indices) {
        if (!isCompatible(val1.elements[i], tuple2.elements[i])) {
            return false
        }
    }
    return true
}

fun isCompatible(
    val1: TacStackValue,
    val2: TacStackValue,
): Boolean {
    if (val1.valueTypes.isEmpty() || val1.valueTypes.contains(TvmSpecType.ANY)) return true
    if (val2.valueTypes.isEmpty() || val2.valueTypes.contains(TvmSpecType.ANY)) return true

    if (val1.valueTypes.contains(TvmSpecType.NULL) || val2.valueTypes.contains(TvmSpecType.NULL)) {
        return true
    }

    if (val1.valueTypes != val2.valueTypes) return false

    return when {
        val1 is TacTupleValue && val2 is TacTupleValue -> tupleCompatible(val1, val2)

        val1 is TacTupleValue || val2 is TacTupleValue -> false

        val1 is ContinuationValue && val2 is ContinuationValue ->
            val1.continuationRef == val2.continuationRef

        val1 is ContinuationValue || val2 is ContinuationValue -> false

        else -> true
    }
}

/**
 * Checks if two execution states (registers and tuple definitions) are compatible
 * to be merged after branching.
 *
 * @return A Pair where:
 *         - first: A description of the incompatibility (error message) or "States are compatible".
 *         - second: Boolean indicating success (true if compatible, false otherwise).
 */
fun areStatesCompatible(
    state1: RegisterState,
    state2: RegisterState,
): Pair<String, Boolean> {
    val regKeys1 = state1.controlRegisters.keys
    val regKeys2 = state2.controlRegisters.keys
    if (regKeys1 != regKeys2) {
        val missingIn1 = regKeys2 - regKeys1
        val missingIn2 = regKeys1 - regKeys2
        val errorString =
            buildString {
                append("Control register keys mismatch: ")
                if (missingIn1.isNotEmpty()) append("missing in state1: $missingIn1")
                if (missingIn1.isNotEmpty() && missingIn2.isNotEmpty()) append("; ")
                if (missingIn2.isNotEmpty()) append("missing in state2: $missingIn2")
            }
        return Pair(errorString, false)
    }

    for (key in regKeys1) {
        val value1 = state1.controlRegisters.getValue(key)
        val value2 = state2.controlRegisters.getValue(key)
        if (!isCompatible(value1, value2)) {
            val errorString =
                "Conflict in control register c_$key: " +
                    "state1 has $value1 (types: ${value1.valueTypes}), " +
                    "state2 has $value2 (types: ${value2.valueTypes})"
            return Pair(errorString, false)
        }
    }

    val globalKeys1 = state1.globalVariables.keys
    val globalKeys2 = state2.globalVariables.keys
    if (globalKeys1 != globalKeys2) {
        if (globalKeys1 != globalKeys2) {
            val missingIn1 = globalKeys2 - globalKeys1
            val missingIn2 = globalKeys1 - globalKeys2
            val errorString =
                buildString {
                    append("Global variable keys mismatch: ")
                    if (missingIn1.isNotEmpty()) append("missing in state1 indexes: $missingIn1")
                    if (missingIn1.isNotEmpty() && missingIn2.isNotEmpty()) append("; ")
                    if (missingIn2.isNotEmpty()) append("missing in state2 indexes: $missingIn2")
                }
            return Pair(errorString, false)
        }
    }

    for (key in globalKeys1) {
        val val1 = state1.globalVariables.getValue(key)
        val val2 = state2.globalVariables.getValue(key)

        if (!isCompatible(val1, val2)) {
            if (val1 is TacTupleValue && val2 is TacTupleValue) {
                if (val1.elements.size != val2.elements.size) {
                    return "Global #$key tuple size mismatch: ${val1.elements.size} vs ${val2.elements.size}" to false
                }

                for (i in val1.elements.indices) {
                    val elem1 = val1.elements[i]
                    val elem2 = val2.elements[i]
                    if (!isCompatible(elem1, elem2)) {
                        return "Global $key tuple element mismatch at index $i: " +
                            "state1 has $elem1 (types: ${elem1.valueTypes}), " +
                            "state2 has $elem2 (types: ${elem2.valueTypes})" to false
                    }
                }
            }

            val errorString =
                "Conflict in global variable $key: " +
                    "state1 has $val1 (types: ${val1.valueTypes}), " +
                    "state2 has $val2 (types: ${val2.valueTypes})"
            return Pair(errorString, false)
        }
    }

    return Pair("States are compatible", true)
}

fun areStacksCompatible(
    stack1: Stack,
    stack2: Stack,
): Pair<String, Boolean> {
    if (stack1.size != stack2.size) {
        return "Stack size mismatch: ${stack1.size} vs ${stack2.size}" to false
    }

    val entries1 = stack1.copyEntries()
    val entries2 = stack2.copyEntries()

    for (i in entries1.indices) {
        val val1 = entries1[i]
        val val2 = entries2[i]

        if (!isCompatible(val1, val2)) {
            if (val1 is TacTupleValue && val2 is TacTupleValue) {
                if (val1.elements.size != val2.elements.size) {
                    return "Stack index $i: Tuple size mismatch (${val1.elements.size} vs ${val2.elements.size})" to
                        false
                }
            }

            return "Stack mismatch at index $i: $val1 vs $val2" to false
        }
    }

    return "Stacks are compatible" to true
}
