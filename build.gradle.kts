plugins {
    kotlin("jvm") version "1.4.10"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    jcenter()
}

application {
    mainClassName = "ApplicationKt"
}

dependencies {
    implementation("io.ktor:ktor-server-netty:1.4.2")
    implementation("io.ktor:ktor-jackson:1.4.2")
    implementation("io.ktor:ktor-auth:1.4.2")
    implementation("org.mongodb:mongodb-driver-sync:4.1.1")
}
