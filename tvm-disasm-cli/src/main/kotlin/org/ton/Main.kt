package org.ton

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ton.disasm.TvmDisassembler

class JsonDisassemblerCommand : CliktCommand(name = "json", help = "Disassemble BoC into json with TVM instructions.") {
    private val bocPath by argument(name = "input", help = "The path to the smart contract in the BoC format")
        .path(mustExist = true, canBeFile = true, canBeDir = false)

    override fun run() {
        val bocContent = bocPath.toFile().readBytes()
        val result = TvmDisassembler.disassemble(bocContent)

        val pretty = Json { prettyPrint = true }
        echo(pretty.encodeToString(result))
    }
}

class TvmDisassemblerCommand : NoOpCliktCommand()

fun main(args: Array<String>) = TvmDisassemblerCommand()
    .subcommands(JsonDisassemblerCommand())
    .main(args)
