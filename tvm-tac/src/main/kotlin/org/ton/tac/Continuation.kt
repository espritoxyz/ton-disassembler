package org.ton.tac

data class ContinuationRef(val label: String) {
    companion object {
        private var ContCounter = 0
        fun next(): ContinuationRef = ContinuationRef("cont_${ContCounter++}")
    }
}