package org.ton.tac

import org.ton.bytecode.disassembleBoc
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TacTest {

    @Test
    @Ignore
    fun testContractFromTact() {
        val path = getResourcePath<TacTest>("/samples/contract-from-tact.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateTacContractCode(contract)
    }

    @Test
    @Ignore
    fun testContractFromTactDebug() {
        val path = getResourcePath<TacTest>("/samples/contract-from-tact.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateTacContractCode(contract)
    }

    @Test
    fun testArrayDebug() {
        val path = getResourcePath<TacTest>("/samples/array.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateDebugTacContractCode(contract)
    }

    @Test
    fun testTwoReturns() {
        val path = getResourcePath<TacTest>("/samples/two_returns.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateTacContractCode(contract)

        assertTrue { tacCode.methods.size == 1 }

        val method = tacCode.methods.values.single()
        checkTwoReturnsWithSingleValue(method)
    }

    @Test
    fun testTwoReturnsDebug() {
        val path = getResourcePath<TacTest>("/samples/two_returns.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateDebugTacContractCode(contract)

        assertTrue { tacCode.methods.size == 1 }

        val method = tacCode.methods.values.single()
        checkTwoReturnsWithSingleValue(method)
    }

    private fun checkTwoReturnsWithSingleValue(method: TacMethod<AbstractTacInst>) {
        var returnInsts = 0
        traverseTacCode(method.instructions) { inst ->
            if (inst !is TacReturnInst) {
                return@traverseTacCode
            }

            returnInsts += 1

            assertEquals(1, inst.result.size)
        }

        assertEquals(2, returnInsts)
    }

    private inline fun <reified T> getResourcePath(path: String): Path {
        return T::class.java.getResource(path)?.path?.let { Path(it) }
            ?: error("Resource $path was not found")
    }
}
