package org.ton.bytecode

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertTrue

class TvmDisasmApiTest {

    @Test
    fun testLongInt() {
        val path = getResourcePath<TvmDisasmApiTest>("/samples/longint.boc")
        val contractCode = disassembleBoc(path)
        assertTrue { contractCode.methods.isNotEmpty() }
    }

    @Test
    fun testNonStandardMain() {
        val path = getResourcePath<TvmDisasmApiTest>("/samples/UQC2PxkHrDqV8ZIzR9EVrte9qR78gHZp15QZNF1G1Ge1LgSN.boc")
        val contractCode = disassembleBoc(path)
        assertTrue { contractCode.mainMethod.instList.isNotEmpty() }
    }

    @Test
    fun testContractWithRecvExternal() {
        val path = getResourcePath<TvmDisasmApiTest>("/samples/UQAfWAbHPQO7yW637r8WBn8fLo4nDPoW1XABqp6vdFbwCFsx.boc")
        val contractCode = disassembleBoc(path)
        assertTrue { MethodId.valueOf(-1) in contractCode.methods }
    }

    @Test
    fun testWithEmptyCellAsDictPushConstOperand() {
        val path = getResourcePath<TvmDisasmApiTest>("/samples/EQC62pJE0q787DFRcgg1ymGmghrcNbaFyKjo9ZUbr0QL0pmT.boc")
        val contractCode = disassembleBoc(path)
       assertTrue { contractCode.mainMethod.instList.isNotEmpty() }
    }

    @Test
    fun testTactOptimization() {
        val path = getResourcePath<TvmDisasmApiTest>("/samples/sample_Divider.code.boc")
        val contractCode = disassembleBoc(path)
        assertTrue { contractCode.mainMethod.instList.isNotEmpty() }

        val pushContInst = contractCode.mainMethod.instList.firstNotNullOf { it as? TvmConstDataPushcontInst }
        val disassembledContinuation = disassembleCell(pushContInst.c.raw)

        assertTrue { disassembledContinuation.methods.isNotEmpty() }
    }

    private inline fun <reified T> getResourcePath(path: String): Path {
        return T::class.java.getResource(path)?.path?.let { Path(it) }
            ?: error("Resource $path was not found")
    }
}
