pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }

    plugins {
        val kotlinVersion = "1.8.20"
        val composeVersion = "1.4.0-dev-wasm06"

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("org.jetbrains.compose") version composeVersion
    }
}


rootProject.name = "planify"

include(":common")
include(":server")
include(":client:web")
include(":client:desktop")
include(":client:shared")
