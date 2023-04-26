rootProject.name = "kotlin-wasm-benchmarks"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        maven(uri("./kotlin-compiler"))
    }
    resolutionStrategy {
        repositories {
            maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
            maven(uri("./kotlin-compiler"))
            gradlePluginPortal()
        }
    }
    val kotlin_version: String by settings
    plugins {
        kotlin("multiplatform").version(kotlin_version)
    }
}