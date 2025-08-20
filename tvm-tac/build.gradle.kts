plugins {
    id("tvm-disasm.kotlin-conventions")
}

dependencies {
    implementation(project(":tvm-opcodes"))

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    testImplementation("ch.qos.logback:logback-classic:${Versions.logback}")
}
