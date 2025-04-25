rootProject.name = "ton-disassembler"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("tvm-disasm")
include("tvm-disasm-cli")
include("tvm-opcodes")
include("tvm-utils")
include("tvm-tac")
