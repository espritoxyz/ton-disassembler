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

tasks.register("formatAndLintAll") {
    group = "formatting"

    dependsOn(tasks.findByName("formatKotlin"))
    dependsOn(tasks.findByName("lintKotlin"))
}

tasks.register<JavaExec>(TaskNames.GENERATE_INSTRUCTIONS) {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "GenerateInstKt"
    workingDir = rootProject.projectDir
}
