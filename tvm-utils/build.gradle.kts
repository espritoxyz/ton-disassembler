plugins {
    id("tvm-disasm.kotlin-conventions")
    id("org.jmailen.kotlinter") version Versions.kotlinterPluginVersion

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
