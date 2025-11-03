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

tasks.register("formatAndLintAll") {
    group = "formatting"

    dependsOn(tasks.findByName("formatKotlin"))
    dependsOn(tasks.findByName("lintKotlin"))
}