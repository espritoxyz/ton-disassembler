package org.ton.disasm.bytecode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
internal val specJson = Json {
    explicitNulls = false
    ignoreUnknownKeys = true
}

@Serializable
internal data class InstructionsList(
    val instructions: List<InstructionDescription>
)

@Serializable
internal data class InstructionDescription(
    val mnemonic: String,
    @SerialName("since_version")
    val sinceVersion: Int,
    val doc: InstructionDescriptionDoc,
    val bytecode: InstructionBytecodeDescription,
    @SerialName("value_flow")
    val valueFlow: InstructionValueFlowDescription,
    @SerialName("control_flow")
    val controlFlow: ControlFlowValueDescription,
)

@Serializable
internal data class InstructionDescriptionDoc(
    val category: String,
    val description: String,
    val gas: String,
    val fift: String,
)

@Serializable
internal data class InstructionBytecodeDescription(
    val operands: List<InstructionOperandDescription>,
    val prefix: String,
    @SerialName("operands_range_check")
    val operandsRangeCheck: OperandsRangeCheck?,
)

@Serializable
internal data class OperandsRangeCheck(
    val length: Int,
    val from: Int,
    val to: Int,
)

@Serializable
internal data class InstructionOperandDescription(
    val name: String,
    val type: String,
    val size: Int?,
    val bits_length_var_size: Int?,
    val bits_padding: Int?,
    val refs_length_var_size: Int?,
    val refs_add: Int?,
    val completion_tag: Boolean?,
)

@Serializable
internal data class InstructionValueFlowDescription(
    val inputs: InstructionValueFlowValueDescription,
    val outputs: InstructionValueFlowValueDescription,
)

@Serializable
internal data class InstructionValueFlowValueDescription(
    val stack: List<InstructionStackValueDescription>?,
)

@Serializable
sealed class InstructionStackValueDescription {
    abstract val entryType: String
}

@Serializable
@SerialName("simple")
class InstructionStackSimpleValue(
    val name: String,
    val value_types: List<String>? = null,
) : InstructionStackValueDescription() {
    override val entryType: String = "simple"
}

@Serializable
@SerialName("const")
class InstructionStackConstValue(
    val value_type: String,
    val value: JsonElement? = null,
) : InstructionStackValueDescription() {
    override val entryType: String = "const"

    val typedValue: Int?
        get() =
            when (value_type) {
                "Null" -> null
                "Integer" -> value?.jsonPrimitive?.int
                else -> null
            }
}

@Serializable
@SerialName("array")
class InstructionStackArrayValue(
    val name: String,
    val length_var: String,
    val array_entry: List<InstructionStackSimpleValue>
) : InstructionStackValueDescription() {
    override val entryType: String = "array"
}

@Serializable
@SerialName("conditional")
class InstructionStackConditionalValue : InstructionStackValueDescription() {
    override val entryType: String = "conditional"
}

@Serializable
internal data class ControlFlowValueDescription(
    val branches: List<ControlFlowBranchContinuation>,
    val nobranch: Boolean,
)

@Serializable
internal data class ControlFlowBranchContinuation(
    val type: String,
    val var_name: String? = null, // only in 'type: variable'
    val save: Map<String, ControlFlowBranchContinuation>? = null, // continuation is a recursive type
)