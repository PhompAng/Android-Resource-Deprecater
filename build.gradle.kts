import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories { mavenCentral() }
    dependencies { classpath(kotlin("gradle-plugin", "1.3.21")) }
}

plugins {
    idea
    id("org.jetbrains.intellij") version "0.4.9"
    kotlin("jvm") version "1.3.21"
}

group = "app.phompang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2018.3.4"
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(
        """
        <ul>
            <li>1.0.0 first release</li>
        </ul>
        """
    )
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}