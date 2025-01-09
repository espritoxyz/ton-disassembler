rootProject.name = "ton-disassembler"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include("tvm-disasm")
include("tvm-opcodes")