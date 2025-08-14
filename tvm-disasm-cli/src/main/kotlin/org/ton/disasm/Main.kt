package org.ton.disasm

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.ParameterHolder
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.groups.MutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.groups.single
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.ton.bytecode.TvmContractCode
import org.ton.bytecode.disassembleBoc
import org.ton.bytecode.prettyPrint
import org.ton.net.TONCENTER_API_V3
import org.ton.net.makeRequest
import org.ton.net.toUrlAddress
import org.ton.tac.dumpTacContract
import org.ton.tac.generateTacContractCode
import java.nio.file.Path
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed interface ContractCode {
    @JvmInline
    value class Boc(val bocPath: Path) : ContractCode

    @JvmInline
    value class Address(val address: String) : ContractCode
}

@OptIn(ExperimentalEncodingApi::class)
fun fetchContractCode(contractCode: ContractCode): ByteArray {
    return when (contractCode) {
        is ContractCode.Boc -> contractCode.bocPath.toFile().readBytes()
        is ContractCode.Address -> {
            val addressForUrl = contractCode.address.toUrlAddress()
            val (_, responseGeneralInfo) = runCatching {
                makeRequest("$TONCENTER_API_V3/addressInformation?use_v2=false&address=$addressForUrl")
            }.getOrElse { error ->
                error("TonAPI request failed for address $addressForUrl: $error")
            }

            val jsonResponse = Json.parseToJsonElement(responseGeneralInfo)
            val codeField = jsonResponse.jsonObject["code"]!!
            check(codeField !is JsonNull) {
                error("No code found for address $addressForUrl - it seems to be an uninitialized contract")
            }

            val base64Code = codeField.jsonPrimitive.content

            // TonCenter API returns base64 encoded code
            Base64.decode(base64Code)
        }
    }
}

fun ParameterHolder.contractCodeOption(): MutuallyExclusiveOptions<ContractCode, ContractCode> {
    return mutuallyExclusiveOptions(
        option("--boc")
            .help("The path to the smart contract in the BoC format")
            .path(mustExist = true, canBeFile = true, canBeDir = false)
            .convert { ContractCode.Boc(it) },
        option("--address")
            .help("The address of the contract deployed on the blockchain")
            .convert { ContractCode.Address(it) }
    ).single().required()
}

class JsonDisassemblerCommand : CliktCommand(
    name = "json",
    help = "Disassemble contract code into json with TVM instructions."
) {
    private val contractCode: ContractCode by contractCodeOption()

    override fun run() {
        val contractCodeSource = contractCode
        val bocContent = fetchContractCode(contractCodeSource)
        val result = TvmDisassembler.disassemble(bocContent)

        val pretty = Json { prettyPrint = true }
        echo(pretty.encodeToString(result))
    }
}

class PrettyPrintDisassemblerCommand : CliktCommand(
    name = "pretty-print",
    help = "Disassemble contract code and pretty print TVM instructions."
) {
    private val contractCode: ContractCode by contractCodeOption()

    private val includeTvmCell: Boolean by option("--include-cell")
        .help("Include TvmCell in the output")
        .flag(default = false)

    override fun run() {
        val contractCodeSource = contractCode
        val bocContent = fetchContractCode(contractCodeSource)
        val disassembledFile: TvmContractCode = disassembleBoc(bocContent)
        val formattedStr = disassembledFile.prettyPrint(includeTvmCell)
        echo(formattedStr)
    }
}

class TacDisassemblerCommand :
    CliktCommand(
        name = "tac",
        help = "Disassemble contract code and output Three-Address Code.",
    ) {
    private val contractCode: ContractCode by contractCodeOption()
    private val includeTvmCell: Boolean by option("--include-cell")
        .help("Include TvmCell in the output")
        .flag(default = false)
    private val debug: Boolean by option("--debug")
        .help("Enable debug output: stack state and instructions")
        .flag(default = false)

    override fun run() {
        val bocContent = fetchContractCode(contractCode)
        val contract = disassembleBoc(bocContent)

        val tacCode = generateTacContractCode(contract)
        val tacOutput = dumpTacContract(tacCode, includeTvmCell)
        echo(tacOutput)
    }
}

class TvmDisassemblerCommand : NoOpCliktCommand()

fun main(args: Array<String>) =
    TvmDisassemblerCommand()
        .subcommands(
            JsonDisassemblerCommand(),
            PrettyPrintDisassemblerCommand(),
            TacDisassemblerCommand(),
        ).main(args)
