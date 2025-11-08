plugins {
    id("tvm-disasm.kotlin-conventions")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("org.jmailen.kotlinter") version Versions.kotlinterPluginVersion
}

dependencies {
    implementation("org.ton:ton-kotlin-tvm:0.3.1")
    implementation("org.ton:ton-kotlin-hashmap-tlb:0.3.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinx_serialization}")
}

val pathToSpec = File(rootProject.projectDir, "tvm-spec/cp0.json")

tasks.processResources {
    from(pathToSpec)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
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
