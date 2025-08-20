# TON Disassembler

TON Disassembler is a tool for disassembling the bytecode of [TVM (TON Virtual Machine)](https://docs.ton.org/tvm.pdf), written in Kotlin.

TON Disassembler can be used as a standalone CLI tool or as a library that can be integrated into JVM-based applications.

## CLI Tool

To use TON Disassembler, you can either download prebuilt artifacts or build the tool from the source code.

### Prebuilt Artifacts

Prebuilt artifacts are available on the [Releases page](https://github.com/espritoxyz/ton-disassembler/releases/latest). Download the `tvm-disasm-cli.jar` file from the release page.

A [JRE](https://www.java.com/en/download) installation is required to run the tool.

You can launch the tool with the following command:

```bash
java -jar tvm-disasm-cli.jar
```

### Building from Source

Make sure the following dependencies are installed on your device:

- [Gradle](https://gradle.org/)
- JDK version 1.8 or higher (e.g., from [here](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html))

To build the same runnable artifact as mentioned above, run the following command from the root of the repository:

```bash
./gradlew tvm-disasm-cli:shadowJar
```

The generated JAR file will be located at:

```
tvm-disasm-cli/build/libs/tvm-disasm-cli.jar
```

### Using the CLI Tool

TON Disassembler CLI can be used in three modes:

1. Disassembling compiled smart contract code into a JSON format containing internal instructions used by [TSA](https://github.com/espritoxyz/tsa).
2. Generating a human-readable list of TVM instructions with arguments in a format similar to [TVM Retracer](https://retracer.ton.org).
3. Generating three-address code with inputs and outputs for every TVM instruction.

The CLI tool accepts compiled smart contract code either as a [BoC](https://docs.ton.org/v3/documentation/data-formats/tlb/cell-boc) file or as an on-chain address.

#### JSON Output

For example, to run the CLI tool on a contract with the address `EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g`:

```bash
java -jar tvm-disasm-cli.jar json --address=EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g > output.json
```

The `output.json` file will contain the following structured data:

```json
"methods": {
  "0": {
    "id": "0",
    "instList": [
      {
        "type": "PUSH",
        "location": {
          "type": "TvmInstMethodLocation",
          "methodId": "0",
          "index": 0
        },
        "i": 0
      },
      {
        "type": "SEMPTY",
        "location": {
          "type": "TvmInstMethodLocation",
          "methodId": "0",
          "index": 1
        }
      },
      {
        "type": "PUSHCONT_SHORT",
        "location": {
          "type": "TvmInstMethodLocation",
          "methodId": "0",
          "index": 2
        },
        "c": [
          {
            "type": "BLKDROP",
            "location": {
              "type": "TvmInstLambdaLocation",
              "index": 0
            },
            "i": 3
          }
        ]
      },
      ...
    ]
  }
}
```

#### Instruction List with Arguments

In this mode, running the CLI tool on the same smart contract using the following command:

```bash
java -jar tvm-disasm-cli.jar pretty-print --address=EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g > output.txt
```

will produce an `output.txt` file with the following content:

```
Main method instructions:
SETCP n=0
DICTPUSHCONST d=[Cell], n=19
DICTIGETJMPZ
THROWARG n=11

Methods instructions:
Method ID: 0
PUSH i=0
SEMPTY
PUSHCONT_SHORT <{
  BLKDROP i=3
}>
IFJMP
XCHG_0I i=1
CTOS
LDU c=3
XCHG_0I i=1
PUSHINT_4 i=1
AND
...
```

#### Three-address code representation

In this mode, all stack instructions (except DEPTH, CHKDEPTH, ONLYTOPX and ONLYX) are processed and removed from code representation.

You can see the result of printing TAC using the following command
```bash
java -jar tvm-disasm-cli.jar tac --address EQAyQ-wYe8U5hhWFtjEWsgyTFQYv1NYQiuoNz6H3L8tcPG3g > output.txt
```
It will produce an `output.txt` file with the following content:
```
Main method:
function (arg0: Integer) {
    SETCP(n=0)
    D_0, n_1 = DICTPUSHCONST(d=[Cell], n=19)
    i_2 = DICTIGETJMPZ(n_1, D_0, arg0)
    THROWARG(n=11, i_2)
}

Method ID: 0
function (arg0: Slice, arg1: Cell, arg2) {
    result_3 = SEMPTY(arg0)
    c_4 = PUSHCONT_SHORT()
    IFJMP(result_3) {
        return 
    }
    s_5 = CTOS(arg1)
    x_6, s2_7 = LDU(c=3, s_5)
    x_8 = PUSHINT_4(i=1)
    result_9 = AND(x_8, x_6)
    ...
```
You can also use `--include-cell` option to include cell contents in the output (in both Instruction List with Arguments and Three-address code representation).
If a cell represents blockchain address, the tool will automatically display it in [raw address format](https://docs.ton.org/v3/concepts/dive-into-ton/ton-blockchain/smart-contract-addresses#raw-address)

#### Current Limitations of TAC feature

The current three-address code representation does not yet support the following TVM features:

- **Array & conditional stack variables**  
  Stack variables of type [`array`](https://github.com/ton-community/tvm-spec?tab=readme-ov-file#array) (e.g. values constructed by `TUPLE`) and [`conditional`](https://github.com/ton-community/tvm-spec?tab=readme-ov-file#conditional) (used by instructions like `UNTIL`) are not supported.

- **Instructions with dynamic parameters**  
  Instructions with an unconstrained number of input or output parameters are not supported (e.g. `RUNVM` instruction).

- **Non-variable continuations**  
  Only continuations of type [`variable`](https://github.com/ton-community/tvm-spec?tab=readme-ov-file#variable-1) are supported.
  Moreover, we currently support only *variable continuations* whose `save` field is either empty, or contains only `c0` continuations of type `cc`. For example, `IF` instruction contains continuation like this in its `save` field:
   ```json
   {
     "type": "variable",
     "var_name": "c",
     "save": {
       "c0": {
         "type": "cc"
         ...
       }
     }
   }
    ```
- **Instructions that pass control flow to control registers**  
  Instructions that transfer execution to Continuations stored in control registers (e.g., `RETALT`) are not supported.


## Library Integration

To integrate TON Disassembler into a JVM-based application, use the `tvm-opcodes` module. It can be included either by using a manually built JAR file or via a build system.

For example, in `Gradle`, you can add the dependency using JitPack:

```gradle
implementation(group = "com.github.espritoxyz.ton-disassembler", name = "tvm-opcodes", version = "b97117fd21c423179162bc9224e447ed50a66f62")
```

(Version corresponds to the commit hash on GitHub.)

Access to the disassembler is provided via the method `org.ton.bytecode.TvmDisasmApiKt#disassembleBoc(java.nio.file.Path)`, located in `tvm-opcodes/src/main/kotlin/org/ton/bytecode/TvmDisasmApi.kt`.

