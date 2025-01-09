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

    private inline fun <reified T> getResourcePath(path: String): Path {
        return T::class.java.getResource(path)?.path?.let { Path(it) }
            ?: error("Resource $path was not found")
    }
}