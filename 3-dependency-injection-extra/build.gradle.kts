plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"
}

group = "com.github.caay2000"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Wrapper> {
    gradleVersion = "7.5"
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.2.1")
    implementation("io.ktor:ktor-server-netty:2.2.1")
    implementation("io.ktor:ktor-server-content-negotiation:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("io.ktor:ktor-server-test-host:2.2.1")
}
