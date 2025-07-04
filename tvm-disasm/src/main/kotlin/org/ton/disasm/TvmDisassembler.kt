package org.ton.disasm

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import org.ton.bitstring.BitString
import org.ton.bitstring.ByteBackedBitString
import org.ton.bitstring.ByteBackedMutableBitString
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellSlice
import org.ton.cell.CellType
import org.ton.disasm.bytecode.CellOperandType
import org.ton.disasm.bytecode.InstructionDescription
import org.ton.disasm.bytecode.dictPushConstMnemonic
import org.ton.disasm.bytecode.opcodeToRefOperandType
import org.ton.disasm.bytecode.opcodeToSubSliceOperandType
import org.ton.disasm.bytecode.pfxDictConstGetJmpMnemonic
import org.ton.disasm.trie.TrieMap
import org.ton.disasm.trie.TrieMapVertex
import org.ton.disasm.utils.HashMapESerializer
import org.ton.disasm.utils.binaryStringToSignedBigInteger
import org.ton.hashmap.HashMapE

data object TvmDisassembler {
    private const val SPEC_PATH_STRING: String = "/cp0.json"
    private val trie: TrieMap by lazy {
        val specStream = this.javaClass.getResourceAsStream(SPEC_PATH_STRING)
            ?: error("Spec not found at path $SPEC_PATH_STRING")

        specStream.use {
            TrieMap.construct(it)
        }
    }

    // like in here: https://github.com/tact-lang/ton-opcode/blob/7f70823f67f3acf73556a187403b281f6e72d15d/src/decompiler/decompileAll.ts#L28
    private val defaultRootForContinuation = listOf(
        dictPushConstMnemonic,
        "DICTIGETJMPZ",
        "THROWARG",
    )

    private val defaultRoot = listOf("SETCP") + defaultRootForContinuation

    private val standardMainMethods = listOf(defaultRoot, defaultRootForContinuation)

    fun disassemble(codeBoc: ByteArray): JsonObject {
        val codeAsCell = BagOfCells(codeBoc).roots.first()
        return disassemble(codeAsCell)
    }

    fun disassemble(codeAsCell: Cell): JsonObject {
        require(codeAsCell.type != CellType.LIBRARY_REFERENCE) {
            "Library cells are not supported"
        }

        val (methods, mainMethod) = disassembleInner(codeAsCell)

        return JsonObject(
            mapOf(
                "mainMethod" to JsonObject(
                    mapOf(
                        "instList" to serializeInstList(mainMethod),
                    )
                ),
                "methods" to serializeMethodMap(methods),
            )
        )
    }

    private fun serializeMethodMap(methods: Map<String, List<TvmInst>>): JsonObject =
        JsonObject(
            methods.entries.associate { (methodId, inst) ->
                methodId to JsonObject(
                    mapOf(
                        "id" to JsonPrimitive(methodId),
                        "instList" to serializeInstList(inst),
                    )
                )
            }
        )

    private fun serializeInstList(instList: List<TvmInst>) =
        JsonArray(instList.map { it.toJson() })

    private fun disassembleInner(cell: Cell): Pair<Map<String, List<TvmInst>>, List<TvmInst>> {
        val slice = cell.beginParse()
        val initialLocation = TvmMainMethodLocation(index = 0)

        val insts = disassemble(slice, initialLocation, cell.hash().toHex())

        val defaultMain = standardMainMethods.any { matchesMnemonics(insts, it) }

        val methods = if (defaultMain) {
            val dictInst = insts.firstNotNullOf { it as? TvmConstDictInst }
            disassembleDictWithMethods(dictInst.dict)
        } else {
            emptyMap()
        }

        return methods to insts
    }

    private fun matchesMnemonics(insts: List<TvmInst>, mnemonics: List<String>): Boolean {
        return insts.size == mnemonics.size && (insts zip mnemonics).all { it.first.type == it.second }
    }

    private fun disassembleDictWithMethods(dict: Map<String, Cell>): Map<String, List<TvmInst>> {
        val result = hashMapOf<String, List<TvmInst>>()

        dict.forEach { (newMethodId, codeCell) ->
            val newInitialLocation = TvmInstMethodLocation(newMethodId, index = 0)
            result[newMethodId] = disassemble(codeCell.beginParse(), newInitialLocation, codeCell.hash().toHex())
        }

        return result
    }

    private fun disassemble(
        slice: CellSlice,
        initialLocation: TvmInstLocation,
        cellHashHex: String,
    ): List<TvmInst> {
        val resultInstructions = mutableListOf<TvmInst>()

        var location = initialLocation

        while (slice.remainingBits > 0) {
            val physicalInstLocation = TvmPhysicalInstLocation(
                cellHashHex = cellHashHex,
                offset = slice.bitsPosition,
            )

            val instDescriptor = getInstructionDescriptor(slice)

            val inst = parseInstruction(slice, instDescriptor, location, physicalInstLocation)

            location = location.increment()
            resultInstructions.add(inst)
        }
        while (slice.refsPosition < slice.refs.size) {
            val nextCell = slice.loadRef()

            val insts = disassemble(nextCell.beginParse(), location, nextCell.hash().toHex())
            location = location.increment(insts.size)

            resultInstructions.addAll(insts)
        }

        return resultInstructions
    }

    private fun getInstructionDescriptor(slice: CellSlice): InstructionDescription {
        var position: TrieMapVertex? = trie.root
        while (position != null) {

            val curInst = position.inst
            if (curInst != null && operandRangeCheck(slice, curInst)) {
                return curInst
            }

            check(slice.remainingBits > 0) {
                "Slice must not be empty at this point, but it is"
            }
            val bit = slice.loadBit()
            position = position.step(bit)
        }

        error("Could not load next instruction for slice $slice")
    }

    private fun operandRangeCheck(slice: CellSlice, instDescriptor: InstructionDescription): Boolean {
        val rangeCheck = instDescriptor.bytecode.operandsRangeCheck
            ?: return true

        val value = slice.preloadUInt(rangeCheck.length)
        return value >= rangeCheck.from.toBigInteger() && value <= rangeCheck.to.toBigInteger()
    }

    private fun parseInstruction(
        slice: CellSlice,
        instDescriptor: InstructionDescription,
        location: TvmInstLocation,
        physicalInstLocation: TvmPhysicalInstLocation,
    ): TvmInst {
        val name = instDescriptor.mnemonic
        var operandsInfo = instDescriptor.bytecode.operands

        // to parse dict, we need parameter n, but in spec the first parameter is dict.
        // since one operand is ref, and another is a part of current slice, reversing is valid.
        if (name == dictPushConstMnemonic) {
            operandsInfo = operandsInfo.reversed()
        }

        val operandsValue = hashMapOf<String, JsonElement>()
        var parsedOperandMap: Map<String, Cell>? = null

        operandsInfo.forEach { operand ->
            val value = when (operand.type) {
                "int" -> {
                    val size = operand.size
                        ?: error("Size of operand $operand not found")
                    parseInt(slice, size)
                }

                "uint" -> {
                    val size = operand.size
                        ?: error("Size of operand $operand not found")
                    parseUInt(slice, size)
                }

                "ref" -> {
                    val (value, map) = parseRef(slice, name, operandsValue)
                    parsedOperandMap = map
                    value
                }

                "pushint_long" -> {
                    parsePushIntLong(slice)
                }

                "subslice" -> {
                    val bitLengthVarSize = operand.bits_length_var_size ?: 0
                    val refLengthVarSize = operand.refs_length_var_size ?: 0
                    val bitPadding = operand.bits_padding ?: 0
                    val refsAdd = operand.refs_add ?: 0
                    val completionTag = operand.completion_tag ?: false
                    parseSubSlice(
                        slice,
                        bitLengthVarSize,
                        refLengthVarSize,
                        bitPadding,
                        refsAdd,
                        completionTag,
                        name,
                        physicalInstLocation.cellHashHex,
                    )
                }

                else -> {
                    error("Unexpected operand type: ${operand.type}")
                }
            }

            value.let { operandsValue[operand.name] = value }
        }

        return parsedOperandMap?.let {
            TvmConstDictInst(name, location, operandsValue, physicalInstLocation, it)
        } ?: TvmInst(name, location, operandsValue, physicalInstLocation)
    }

    private fun parseInt(slice: CellSlice, size: Int): JsonPrimitive {
        val result = slice.loadInt(size).toInt()
        return JsonPrimitive(result)
    }

    private fun parseUInt(slice: CellSlice, size: Int): JsonPrimitive {
        val result = slice.loadUInt(size).toInt()
        return JsonPrimitive(result)
    }

    private fun parseDict(
        dictCell: Cell,
        keySize: Int,
    ): Map<String, Cell> {
        if (dictCell.isEmpty()) {
            return emptyMap()
        }

        val wrappedRef = Cell(BitString(listOf(true)), dictCell)

        val map = HashMapE.tlbCodec(keySize, HashMapESerializer).loadTlb(wrappedRef)

        return map.toMap().map { (key, value) ->
            val keyAsBigInt = key.toBinary().binaryStringToSignedBigInteger()
            keyAsBigInt.toString() to value
        }.toMap()
    }

    private fun parseDictPushConst(
        ref: Cell,
        operands: Map<String, JsonElement>,
    ): Map<String, Cell> {
        val keySize = operands["n"]?.jsonPrimitive?.int
            ?: error("No 'n' parameter for DICTPUSHCONST")

        return parseDict(ref, keySize)
    }

    private fun parseRef(
        slice: CellSlice,
        opname: String,
        operands: Map<String, JsonElement>,
    ): Pair<JsonElement, Map<String, Cell>> {
        val ref = slice.loadRef()

        val givenOperandType = opcodeToRefOperandType[opname]
            ?: error("Unexpected opcode with ref operand: $opname")

        if (opname == dictPushConstMnemonic) {
            val rawDict = serializeSlice(ref.beginParse())
            val resultMap = parseDictPushConst(ref, operands)
            return rawDict to resultMap
        }

        val operandType = if (opname == pfxDictConstGetJmpMnemonic) {
            // TODO: maybe process this case later
            CellOperandType.OrdinaryCell
        } else {
            givenOperandType
        }

        when (operandType) {
            CellOperandType.CodeCell -> {
                val newLocation = TvmInstLambdaLocation(0)
                val insts = disassemble(
                    ref.beginParse(),
                    newLocation,
                    ref.hash().toHex(),
                )
                val raw = serializeSlice(ref.beginParse())
                return serializeInstListOperand(insts, raw) to emptyMap()
            }

            CellOperandType.OrdinaryCell -> {
                return serializeSlice(ref.beginParse()) to emptyMap()
            }

            CellOperandType.SpecialCell -> {
                error("Unexpected opname with special ref: $opname")
            }
        }
    }

    private fun parsePushIntLong(slice: CellSlice): JsonPrimitive {
        val prefix = slice.loadUInt(5).toInt()
        val length = 8 * prefix + 19
        val result = slice.loadInt(length)
        return JsonPrimitive(result.toString())
    }

    private fun parseSubSlice(
        slice: CellSlice,
        bitLengthVarSize: Int,
        refLengthVarSize: Int,
        bitPadding: Int,
        refsAdd: Int,
        completionTag: Boolean,
        opname: String,
        cellHashHex: String,
    ): JsonElement {

        val operandType = opcodeToSubSliceOperandType[opname]
            ?: error("Unexpected opcode with subslice operand: $opname")

        val refsLength = slice.loadUInt(refLengthVarSize).toInt() + refsAdd
        val bitsLength = slice.loadUInt(bitLengthVarSize).toInt() * 8 + bitPadding

        check(bitsLength <= slice.remainingBits) {
            "Not enough bits (less than $bitsLength) in slice $slice"
        }

        val originalBits = slice.bits
        val originalBitsAsBytearray = if (originalBits is ByteBackedBitString) {
            originalBits.bytes
        } else {
            originalBits.toByteArray()
        }

        val bits = ByteBackedMutableBitString(originalBitsAsBytearray, slice.bitsPosition + bitsLength)
        while (completionTag && !bits.last()) {
            bits.size--
        }
        if (completionTag) {
            bits.size--
        }

        val refs = List(refsLength) { slice.loadRef() }
        val newSlice = CellSlice.of(bits, refs)
        newSlice.bitsPosition = slice.bitsPosition

        slice.skipBits(bitsLength)

        when (operandType) {
            CellOperandType.CodeCell -> {
                val newLocation = TvmInstLambdaLocation(0)
                val raw = serializeSlice(newSlice)
                val insts = disassemble(
                    newSlice,
                    newLocation,
                    cellHashHex,
                )
                return serializeInstListOperand(insts, raw)
            }

            CellOperandType.OrdinaryCell -> {
                return serializeSlice(newSlice)
            }

            CellOperandType.SpecialCell -> {
                error("Unexpected operation $opname with special subslice operand")
            }
        }
    }

    private fun serializeInstListOperand(insts: List<TvmInst>, raw: JsonElement): JsonObject {
        val list = serializeInstList(insts)
        return JsonObject(
            mapOf(
                "list" to list,
                "raw" to raw,
            )
        )
    }

    private fun serializeSlice(slice: CellSlice): JsonElement {
        return Json.encodeToJsonElement(
            mapOf(
                "_bits" to slice.bits.drop(slice.bitsPosition).map { JsonPrimitive(if (it) 1 else 0) },
                "_refs" to slice.refs.drop(slice.refsPosition).map { serializeSlice(it.beginParse()) },
            )
        )
    }
}
