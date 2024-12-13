val kotlin_version: String by project
val logback_version: String by project
val kotlinx_html_version: String by project
val exposed_version: String by project
val sqlite_version: String by project
val flyway_version: String by project
val selenium_version: String by project
val kotest_version: String by project
val kotest_ktor_version: String by project
val koin_version: String by project
val hikari_version: String by project
val hoplite_version: String by project
val argon2_version: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.0"
}

group = "com.jones"
version = "0.0.1"

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":application:app"))
    implementation(project(":application:security"))
    implementation(project(":features:user"))
    implementation(project(":features:admin"))

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-resources-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")

    testImplementation("org.seleniumhq.selenium:selenium-java:$selenium_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("io.ktor:ktor-client-resources")
    testImplementation("io.ktor:ktor-client-content-negotiation")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:$kotest_ktor_version")
}
