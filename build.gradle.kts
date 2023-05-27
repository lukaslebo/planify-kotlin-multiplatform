plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    id("org.jetbrains.compose") apply false
}

group = "ch.lebo"
version = "1.0-SNAPSHOT"


allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}
