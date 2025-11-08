plugins {
    id("tvm-disasm.kotlin-conventions")
    id("org.jmailen.kotlinter") version Versions.kotlinterPluginVersion
}

dependencies {
    implementation(project(":tvm-opcodes"))

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    testImplementation("ch.qos.logback:logback-classic:${Versions.logback}")
}

val ignoredFiles = listOf("TvmInstructions.kt", "TvmInstructionsDefaults.kt")

tasks.lintKotlinMain {
    exclude {
        it.file.name in ignoredFiles
    }
}

tasks.formatKotlinMain {
    exclude {
        it.file.name in ignoredFiles
    }
}

tasks.register("formatAndLintAll") {
    group = "formatting"

    dependsOn(tasks.findByName("formatKotlin"))
    dependsOn(tasks.findByName("lintKotlin"))
}
