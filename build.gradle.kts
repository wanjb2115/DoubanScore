import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.seleniumhq.selenium:selenium-java:4.7.1")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.7.1")
    testImplementation("org.slf4j:slf4j-log4j12:2.0.6")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}