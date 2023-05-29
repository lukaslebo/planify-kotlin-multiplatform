plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common"))
    implementation("io.ktor:ktor-server-core:2.3.1")
    implementation("io.ktor:ktor-server-netty:2.3.1")
    implementation("io.ktor:ktor-server-websockets:2.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
    implementation("io.ktor:ktor-server-cors:2.3.1")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.1")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.7")
    testImplementation("io.ktor:ktor-server-test-host:2.3.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
