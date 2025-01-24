package org.ton

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.groups.single
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.ton.disasm.TvmDisassembler
import org.ton.net.TONCENTER_API_V3
import org.ton.net.makeRequest
import org.ton.net.toUrlAddress
import java.nio.file.Path
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

sealed interface ContractCode {
    @JvmInline
    value class Boc(val bocPath: Path) : ContractCode

    @JvmInline
    value class Address(val address: String) : ContractCode
}

class JsonDisassemblerCommand : CliktCommand(
    name = "json",
    help = "Disassemble contract code into json with TVM instructions."
) {
    private val contractCode: ContractCode by mutuallyExclusiveOptions(
        option("--boc")
            .help("The path to the smart contract in the BoC format")
            .path(mustExist = true, canBeFile = true, canBeDir = false)
            .convert { ContractCode.Boc(it) },
        option("--address")
            .help("The address of the contract deployed on the blockchain")
            .convert { ContractCode.Address(it) }
    ).single().required()

    @OptIn(ExperimentalEncodingApi::class)
    override fun run() {
        val contractCodeSource = contractCode
        val bocContent = when (contractCodeSource) {
            is ContractCode.Boc -> contractCodeSource.bocPath.toFile().readBytes()
            is ContractCode.Address -> {
                val addressForUrl = contractCodeSource.address.toUrlAddress()
                val (_, responseGeneralInfo) = runCatching {
                    makeRequest("$TONCENTER_API_V3/addressInformation?use_v2=false&address=$addressForUrl")
                }.getOrElse { error ->
                    throw IllegalStateException("TonAPI request failed for address $addressForUrl: $error")
                }
                val base64Code = Json.parseToJsonElement(responseGeneralInfo).jsonObject["code"]!!.jsonPrimitive.content

                // TonCenter API returns base64 encoded code
                Base64.decode(base64Code)
            }
        }
        val result = TvmDisassembler.disassemble(bocContent)

        val pretty = Json { prettyPrint = true }
        echo(pretty.encodeToString(result))
    }
}

class TvmDisassemblerCommand : NoOpCliktCommand()

fun main(args: Array<String>) = TvmDisassemblerCommand()
    .subcommands(JsonDisassemblerCommand())
    .main(args)
