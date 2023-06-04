plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm("desktop")

    @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
    wasm {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(project(":common"))

                implementation("io.ktor:ktor-client-core:2.3.1-wasm0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1-wasm0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.1-wasm0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-RC-wasm0")
            }
        }

        val wasmMain by getting {
            dependencies {
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-java:2.3.1")
            }
        }
    }
}
