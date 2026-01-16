package org.ton.tac

import org.junit.jupiter.api.assertThrows
import org.ton.bytecode.TvmContBasicRetaltInst
import org.ton.bytecode.disassembleBoc
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TacTest {
    @Test
    fun testRetaltGoto() {
        val path = getResourcePath<TacTest>("/samples/retalt-and-goto.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateTacContractCode(contract)
        assertBlockEndsWith(tacCode, TvmContBasicRetaltInst.MNEMONIC) { lastInst ->
            lastInst is TacGotoInst
        }
    }

    @Test
    fun testRetaltReturn() {
        val path = getResourcePath<TacTest>("/samples/retalt-and-return.boc")
        val contract = disassembleBoc(path)
        val tacCode = generateTacContractCode(contract)
        assertBlockEndsWith(tacCode, TvmContBasicRetaltInst.MNEMONIC) { lastInst ->
            lastInst is TacReturnInst
        }
    }

    @Test
    fun testRetalt() {
        val path = getResourcePath<TacTest>("/samples/retalt-basic.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    fun testRetaltDebug() {
        val path = getResourcePath<TacTest>("/samples/retalt-basic.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
    }

    @Test
    fun testBadDict() {
        val path = getResourcePath<TacTest>("/samples/bad-dict.boc")
        val contract = disassembleBoc(path)
        assertThrows<IllegalStateException> {
            generateTacContractCode(contract)
        }
    }

    @Test
    fun testBadDictDebug() {
        val path = getResourcePath<TacTest>("/samples/bad-dict.boc")
        val contract = disassembleBoc(path)
        assertThrows<IllegalStateException> {
            generateDebugTacContractCode(contract)
        }
    }

    @Test
    fun testDict() {
        val path = getResourcePath<TacTest>("/samples/dict-contract.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    fun testDictDebug() {
        val path = getResourcePath<TacTest>("/samples/dict-contract.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
    }

    @Test
    fun testTupleCompatible() {
        val path = getResourcePath<TacTest>("/samples/compatible_tuple.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    fun testTupleCompatibleDebug() {
        val path = getResourcePath<TacTest>("/samples/compatible_tuple.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
    }

    @Test
    fun testTupleUncompatible() {
        val path = getResourcePath<TacTest>("/samples/incompatible.boc")
        val contract = disassembleBoc(path)

        assertThrows<IllegalStateException> {
            generateTacContractCode(contract)
        }
    }

    @Test
    fun testTupleUncompatibleDebug() {
        val path = getResourcePath<TacTest>("/samples/incompatible.boc")
        val contract = disassembleBoc(path)

        assertThrows<IllegalStateException> {
            generateTacContractCode(contract)
        }
    }

    @Test
    fun testIfCtrCompatible() {
        val path = getResourcePath<TacTest>("/samples/compatible.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    fun testIfCtrCompatibleDebug() {
        val path = getResourcePath<TacTest>("/samples/compatible.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
    }

    @Test
    fun testUntuple() {
        val path = getResourcePath<TacTest>("/samples/untuple.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    fun testUntupleDebug() {
        val path = getResourcePath<TacTest>("/samples/untuple.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
    }

    @Test
    fun testArray() {
        val path = getResourcePath<TacTest>("/samples/array.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    fun testArrayDebug() {
        val path = getResourcePath<TacTest>("/samples/array.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
    }

    @Test
    @Ignore
    fun testContractFromTact() {
        val path = getResourcePath<TacTest>("/samples/contract-from-tact.boc")
        val contract = disassembleBoc(path)
        generateTacContractCode(contract)
    }

    @Test
    @Ignore
    fun testContractFromTactDebug() {
        val path = getResourcePath<TacTest>("/samples/contract-from-tact.boc")
        val contract = disassembleBoc(path)
        generateDebugTacContractCode(contract)
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

    private inline fun <reified T> getResourcePath(path: String): Path =
        T::class.java
            .getResource(path)
            ?.path
            ?.let { Path(it) }
            ?: error("Resource $path was not found")
}
