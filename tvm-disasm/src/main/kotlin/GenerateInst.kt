import kotlinx.serialization.decodeFromString
import org.ton.disasm.bytecode.CellOperandType
import org.ton.disasm.bytecode.InstructionDescription
import org.ton.disasm.bytecode.InstructionOperandDescription
import org.ton.disasm.bytecode.InstructionStackConstValue
import org.ton.disasm.bytecode.InstructionStackSimpleValue
import org.ton.disasm.bytecode.InstructionsList
import org.ton.disasm.bytecode.opcodeToRefOperandType
import org.ton.disasm.bytecode.opcodeToSubSliceOperandType
import org.ton.disasm.bytecode.specJson
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.readText
import kotlin.io.use

private val instructionsListPath = Path("tvm-spec/cp0.json")
private val generatedInstPath = Path("tvm-opcodes/src/main/kotlin/org/ton/bytecode/TvmInstructions.kt")
private val defaultInstPath = Path("tvm-opcodes/src/main/kotlin/org/ton/bytecode/TvmInstructionsDefaults.kt")
enum class StackFlowDirection { INPUT, OUTPUT }

private fun generateInstructionCellOperandTypes(
    instructions: Map<String, InstructionDescription>
): Map<String, Map<String, String>> =
    (opcodeToSubSliceOperandType + opcodeToRefOperandType).mapNotNull { (opname, type) ->
        val inst = instructions[opname]
            ?: error("Instruction $opname not found")
        val typeName = when (type) {
            CellOperandType.CodeCell -> {
                "TvmInstList"
            }
            CellOperandType.OrdinaryCell -> {
                "TvmCell"
            }
            CellOperandType.SpecialCell -> {
                return@mapNotNull null
            }
        }
        check(inst.bytecode.operands.isNotEmpty()) {
            "Expected operands for instruction $opname"
        }
        val map = if (inst.bytecode.operands.size == 1) {
            mapOf("c" to typeName)
        } else {
            List(inst.bytecode.operands.size) { index -> "c${index + 1}" to typeName }.toMap()
        }
        opname to map
    }.toMap()

private const val ADDITIONAL_CATEGORY_DICT = "dict"

private val additionalCategories = setOf(ADDITIONAL_CATEGORY_DICT)

private fun snakeToCamel(value: String): String =
    value.split('_').joinToString("") {
        it.replace("-", "Minus").lowercase().replaceFirstChar(Char::titlecase)
    }

private fun tvmInstCategoryClassName(categoryName: String): String =
    "Tvm${snakeToCamel(categoryName)}Inst"

private fun tvmInstClassName(inst: InstructionDescription): String =
    "Tvm${snakeToCamel(inst.doc.category)}${snakeToCamel(inst.mnemonic)}Inst"

private fun tvmInstOperandType(operand: InstructionOperandDescription): String = when (operand.type) {
    "uint", "int" -> "Int"
    "pushint_long" -> "String"
    "ref", "subslice" -> "TvmCell"
    else -> error("Unexpected operand type: $operand")
}

private fun tvmInstCategoryDeclaration(category: String): String {
    var additionalCategories = ""
    if (category != ADDITIONAL_CATEGORY_DICT && category.lowercase().startsWith(ADDITIONAL_CATEGORY_DICT)) {
        additionalCategories += ", ${tvmInstCategoryClassName(ADDITIONAL_CATEGORY_DICT)}"
    }

    return """
        @Serializable
        sealed interface ${tvmInstCategoryClassName(category)}: TvmInst$additionalCategories
    """.trimIndent()
}

private fun normalizeDocString(docStr: String): List<String> =
    docStr.split("\n").flatMap { str ->
        str.chunked(100).map { it.trim() }
    }

private fun tvmInstArgumentTypes(
    inst: InstructionDescription,
    instructionOperandTypes: Map<String, Map<String, String>>,
): List<String?> {
    val result = mutableListOf<String?>()
    for (arg in inst.bytecode.operands) {
        var type = instructionOperandTypes[inst.mnemonic]?.get(arg.name)
        if (type == null) {
            type = tvmInstOperandType(arg)
        }

        result.add(type)
    }

    return result
}

private fun tvmInstDefault(
    inst: InstructionDescription,
    instructionOperandTypes: Map<String, Map<String, String>>,
): String {
    val className = tvmInstClassName(inst)
    val types = tvmInstArgumentTypes(inst, instructionOperandTypes)
    val arguments = mutableListOf<String>()

    arguments += "|            TvmMainMethodLocation(0),"

    for (type in types) {
        if (type == null) {
            continue
        }

        val value = when (type) {
            "Int" -> "0"
            "String" -> "\"0\""
            "TvmCell" -> "TvmCell(TvmCellData(\"\"), emptyList())"
            "TvmInstList" -> "TvmInstList(emptyList())"
            else -> error("Unexpected operand type: $type")
        }

        arguments += "|            $value,"
    }

    return """
        |        $className(
        ${arguments.joinToString("\n")}
        |        ),
    """.trimMargin()
}

private fun extractStackEntries(
    inst: InstructionDescription,
    direction: StackFlowDirection
) : List<String> {
    val entries = when (direction) {
        StackFlowDirection.INPUT -> inst.valueFlow.inputs.stack.orEmpty()
        StackFlowDirection.OUTPUT -> inst.valueFlow.outputs.stack.orEmpty()
    }

    return entries.map { entry ->
        val raw = when (entry) {
            is InstructionStackSimpleValue -> {
                """
                    TvmSimpleStackEntry(
                        name = "${entry.name}",
                        valueTypes = listOf(${entry.value_types.orEmpty().joinToString(", ") { "\"$it\"" }})
                    )
                    """
            }
            is InstructionStackConstValue -> {
                val valueStr = entry.typedValue?.toString() ?: "null"
                """
                TvmConstStackEntry(
                    value = $valueStr,
                    valueType = "${entry.value_type}"
                )
                """
            }
            else -> {
                """
                    TvmGenericStackEntry(
                        type = "${entry.entryType}"
                    )
                    """
            }
        }

        raw.trimIndent().prependIndent("            ")
    }
}

private fun tvmInstDeclaration(
    inst: InstructionDescription,
    instructionOperandTypes: Map<String, Map<String, String>>,
): String {
    val className = tvmInstClassName(inst)
    val arguments = mutableListOf<String>()

    arguments += "|    override val location: TvmInstLocation,"

    val types = tvmInstArgumentTypes(inst, instructionOperandTypes)

    var contArgsCount = 0
    for ((arg, type) in inst.bytecode.operands zip types) {
        if (type == null) {
            continue
        }

        val modifier = if (type == "TvmInstList") {
            contArgsCount++
            "override "
        } else {
            ""
        }

        arguments += "|    ${modifier}val ${arg.name}: ${type}, // ${arg.type}"
    }

    val additionalInterfaces = ", TvmContOperand${contArgsCount}Inst".takeIf {
        contArgsCount > 0
    } ?: ""

    val docs = normalizeDocString(inst.doc.description).joinToString("\n") { "| * $it" }

    val gasUsage = inst.doc.gas.toIntOrNull()
    val tvmGasUsage = when{
        inst.doc.gas.isBlank() -> "TvmSimpleGas"
        gasUsage != null -> "TvmFixedGas(value = $gasUsage)"
        else -> "TvmComplexGas(this, description = \"${inst.doc.gas}\")"
    }

    return """
    |/**
    $docs
    | */
    |@Serializable
    |@SerialName($className.MNEMONIC)
    |data class $className(
    ${arguments.joinToString("\n")}
    |): TvmInst, ${tvmInstCategoryClassName(inst.doc.category)}$additionalInterfaces {
    |    override val mnemonic: String get() = MNEMONIC
    |    override val gasConsumption get() = $tvmGasUsage
    |    override val stackInputs: List<TvmStackEntry>
    |       get() = listOf(
${extractStackEntries(inst, StackFlowDirection.INPUT).joinToString(",\n") {it}}
    |       )
    |    override val stackOutputs: List<TvmStackEntry>
    |       get() = listOf(
${extractStackEntries(inst, StackFlowDirection.OUTPUT).joinToString(",\n") {it}}
    |       )
    |    
    |    companion object {
    |        const val MNEMONIC = "${inst.mnemonic}"
    |    }
    |}
    """.trimMargin()
}

private fun generateInstructionSerializer(instructions: Set<String>): String {
    val instructionSerializers = instructions.joinToString("\n") { "|        subclass($it::class)" }
    return """
    |fun SerializersModuleBuilder.registerTvmInstSerializer() {
    |    polymorphic(TvmInst::class) {
    $instructionSerializers
    |    }
    |}
    """.trimMargin()
}

fun main() {
    val instructions = specJson.decodeFromString<InstructionsList>(instructionsListPath.readText())
    val instructionByMnemonic = instructions.instructions.associateBy { it.mnemonic }

    val basicInstructions = instructions.instructions.associateBy { tvmInstClassName(it) }

    val categories = instructions.instructions.mapTo(mutableSetOf()) { it.doc.category }
        .also { it += additionalCategories }
        .associateWith { tvmInstCategoryDeclaration(it) }

    val instructionOperandTypes = generateInstructionCellOperandTypes(instructionByMnemonic)

    val basicInstructionsDeclarations = basicInstructions.mapValues {
        tvmInstDeclaration(it.value, instructionOperandTypes)
    }

    val categoryToInstructionDefault = categories.keys.associateWith { category ->
        val correspondingInstructions = basicInstructions.values.filter { it.doc.category == category }
        val defaults = correspondingInstructions.map {
            tvmInstDefault(it, instructionOperandTypes)
        }
        defaults
    }

    generatedInstPath.bufferedWriter().use { writer ->
        writer.appendLine(
            """
            // Generated
            package org.ton.bytecode

            import kotlinx.serialization.SerialName
            import kotlinx.serialization.Serializable
            import kotlinx.serialization.modules.SerializersModuleBuilder
            import kotlinx.serialization.modules.polymorphic
            import kotlinx.serialization.modules.subclass
            
            sealed class TvmStackEntry {
                abstract val type: String
            }
            
            data class TvmSimpleStackEntry(
                val name: String,
                val valueTypes: List<String>
            ) : TvmStackEntry() {
                override val type: String = "simple"
            }
            
            data class TvmConstStackEntry(
                val valueType: String,
                val value: Int? = null
            ) : TvmStackEntry() {
                override val type: String = "const"
            }

            data class TvmGenericStackEntry(
                override val type: String
            ) : TvmStackEntry()
            
        """.trimIndent()
        )

        categories.entries.sortedBy { it.key }.forEach {
            writer.appendLine(it.value)
            writer.appendLine()
        }

        basicInstructionsDeclarations.entries.sortedBy { it.key }.forEach {
            writer.appendLine(it.value)
            writer.appendLine()
        }

        val allInstructions = basicInstructionsDeclarations.keys
        writer.appendLine(generateInstructionSerializer(allInstructions))
    }

    defaultInstPath.bufferedWriter().use { writer ->

        writer.appendLine(
            """
            // Generated
            package org.ton.bytecode
            
            val tvmDefaultInstructions = mapOf(
            """.trimIndent()
        )

        categoryToInstructionDefault.entries.forEach { (category, insts) ->
            if (insts.isEmpty()) {
                return@forEach
            }
            writer.appendLine(
                """
                |    "$category" to listOf(
                """.trimMargin()
            )
            insts.forEach {
                writer.appendLine(it)
            }
            writer.appendLine("    ),")
        }
        writer.appendLine(")")
    }
}
