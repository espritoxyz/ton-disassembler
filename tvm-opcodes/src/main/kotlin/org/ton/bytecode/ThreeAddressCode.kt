package org.ton.bytecode

import kotlin.reflect.full.memberProperties

data class DecompiledInstruction(
    val spec: TvmInst,
    val operands: VarMap,
    val inputs: Map<String, StackVariable>,
    val outputs: Map<String, StackVariable>,
    val resultStack: List<StackVariable>,
    val comment: String
)

data class DecompiledMethod(
    val instructions: List<DecompiledInstruction>,
    val args: List<StackVariable>
)

fun decompileInstruction(
    inst: TvmInst,
    operands: VarMap,
    stack: Stack,
    inputSpec: List<TvmStackEntry>,
    outputSpec: List<TvmStackEntry>,
    debug: Boolean = false
): DecompiledInstruction {

    val inputs = mutableMapOf<String, StackVariable>()
    val outputs = mutableMapOf<String, StackVariable>()
    var constCounter = 0


    if (inst is TvmStackBasicInst || inst is TvmStackComplexInst) {
        stack.execStackInstruction(inst)

        val stackInstInfo = if (debug) {
            "${inst.mnemonic} ${extractPrimitiveOperands(inst)} stack: [${stack.dump()}]"
        } else {
            ""
        }

        return DecompiledInstruction(
                spec = inst,
                operands = operands,
                inputs = inputs,
                outputs = outputs,
                resultStack = stack.copyEntries(),
                comment = stackInstInfo
            )
    }

    // Pop inputs in reverse since we deal with stack
    inputSpec.reversed().forEach { input ->
        if (input.type == "simple") {
            val simpleInput = input as TvmSimpleStackEntry
            val inputValues = simpleInput.valueTypes
            inputs[simpleInput.name] = stack.pop(valuesCheck = inputValues)
        } else {
            error("Unsupported input type: \${input.type}")
        }
    }


    var stackInfo: String = stack.getMessageCollector().joinToString("\n")

    stack.clearMessagesCollector()

    outputSpec.forEach { output ->
        when (output.type) {
            "simple" -> {
                val simpleOutput = output as TvmSimpleStackEntry
                val specName = simpleOutput.name
                val valueTypes = simpleOutput.valueTypes
                outputs[specName] = stack.push(specName, inst.mnemonic, valueTypes)
            }
            "const" -> {
                val varName = "const${constCounter++}"
                val valueType = (output as TvmConstStackEntry).valueType
                outputs[varName] = stack.push(varName, inst.mnemonic, listOf(valueType))
            }
            else -> {
                val entryType = output.type
                outputs["_$entryType"] = stack.push(output.type, inst.mnemonic)
            }
        }
    }
    if (debug) stackInfo += " stack: [${stack.dump()}]"

    return DecompiledInstruction(
        spec = inst,
        operands = operands,
        inputs = inputs,
        outputs = outputs,
        resultStack = stack.copyEntries(),
        comment = stackInfo
    )
}




fun decompileMethod(
    method: TvmDisasmCodeBlock,
    debug: Boolean = false
): DecompiledMethod {

    val stack = Stack(emptyList())
    val code = mutableListOf<DecompiledInstruction>()

    for (inst in method.instList) {
        val operands = extractPrimitiveOperands(inst).toMutableMap()

        val inputs = inst.stackInputs
            ?.filter { it.type == "simple" || it.type == "const" } ?: emptyList()
        val outputs = inst.stackOutputs
            ?.filter { it.type == "simple" || it.type == "const" } ?: emptyList()

        val di = decompileInstruction(inst, operands, stack, inputs, outputs, debug)
        code += di
    }

    return DecompiledMethod(code, stack.getCreatedArgs())
}


private fun dumpOperandConts(inst: TvmContOperandInst, includeTvmCell: Boolean = false, debug: Boolean = false): List<String> {
    val result = mutableListOf<String>()

    val continuationProps = inst::class.memberProperties
        .filter { it.returnType.classifier == TvmInstList::class }

    if (continuationProps.isEmpty()) {
        println("[Warning] No continuation blocks found for ${inst::class.simpleName}")
    }

    for (prop in continuationProps) {
        val instList = prop.getter.call(inst)
        if (instList is TvmInstList) {
            val blockCode = dumpTAC(
                TvmInlineBlock(instList.list.toMutableList()), includeTvmCell, debug
            ).trimEnd().prependIndent("    ")

            result += "<{\n$blockCode\n}>"
        }
    }

    return result
}



fun dumpTAC(
    method: TvmDisasmCodeBlock,
    includeTvmCell: Boolean = false,
    debug: Boolean = false
    ): String {
    val codeBuilder = StringBuilder()

    val decompiledMethod = when (method) {
        is TvmMethod -> decompileMethod(method, debug)
        is TvmMainMethod -> decompileMethod(method, debug)
        is TvmInlineBlock -> decompileMethod(method, debug)
        else -> error("Unsupported code block type: ${method::class.simpleName}")
    }

    val args = decompiledMethod.args
    val argList = args.joinToString(", ") {
        if (it.valueTypes.isNotEmpty()) {
            "${it.specName}: ${it.valueTypes.joinToString(" | ")}"
        } else it.specName
    }
    codeBuilder.appendLine("function(${argList}) {")

    val bodyBuilder = StringBuilder()

    for (instruction in decompiledMethod.instructions) {
        val instructionSpec = instruction.spec
        val mutableOperands = extractPrimitiveOperands(instructionSpec).toMutableMap()
        val operandStrings = mutableOperands.map { (key, value) ->
            "$key=${if (includeTvmCell) formatOperand(value) else if (value !is TvmCell) "$value" else "[Cell]"}"
        }
        val inputStr = operandStrings.joinToString(", ")

        val outputVars = instruction.outputs.values.map { it.specName } // reverse them back
        val inputVars = instruction.inputs.values.map { it.specName }.reversed()

        val lhs = if (outputVars.isNotEmpty()) {
            outputVars.joinToString(", ") + " = "
        } else ""

        if (instructionSpec is TvmContOperandInst) {
            val continuationBlocks = dumpOperandConts(instructionSpec)

            continuationBlocks.forEach { cont ->
                bodyBuilder.appendLine(lhs + instructionSpec.mnemonic + cont)
                if (instruction.comment.isNotEmpty()) {
                    bodyBuilder.appendLine("  // ${instruction.comment}")
                }
            }
            continue // Skip normal output
        }

        if (instructionSpec is TvmStackBasicInst || instructionSpec is TvmStackComplexInst) {
            if (debug) bodyBuilder.appendLine(instruction.comment)
            continue
        }

        val rhs = buildString {
            append("${instruction.spec.mnemonic}(")
            append(inputVars.joinToString(", "))
            if (inputStr.isNotEmpty()) {
                if (inputVars.isNotEmpty()) append(", ")
                append(inputStr)
            }
            append(")")
        }

        bodyBuilder.appendLine(lhs + rhs)
        if (instruction.comment.isNotEmpty()) {
            bodyBuilder.appendLine("  // ${instruction.comment}")
        }
    }

    codeBuilder.appendLine(bodyBuilder.toString().trimEnd().prependIndent("  "))
    codeBuilder.append("}")

    return codeBuilder.toString()
}


fun dumpContractTAC(
    contract: TvmContractCode,
    includeTvmCell: Boolean = false,
    debug: Boolean = false,
):String {
    val codeBuilder = StringBuilder()

    codeBuilder.append("Main method: ")
    codeBuilder.appendLine(dumpTAC(contract.mainMethod, includeTvmCell, debug))

    for ((methodId, method) in contract.methods) {
        codeBuilder.append("Method ID: $methodId, ")
        codeBuilder.appendLine(dumpTAC(method, includeTvmCell, debug))
    }
    return codeBuilder.toString()
}