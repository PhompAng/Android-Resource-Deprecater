import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories { mavenCentral() }
    dependencies { classpath(kotlin("gradle-plugin", "1.3.41")) }
}

plugins {
    idea apply true
    id("org.jetbrains.intellij") version "0.4.10"
    kotlin("jvm") version "1.3.41"
    id("org.jetbrains.gradle.plugin.idea-ext") version "0.5" apply true
}

group = "app.phompang"
version = "1.0.3-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    setPlugins("java")
    updateSinceUntilBuild = false
}

val publishPlugin: PublishTask by tasks
val patchPluginXml: PatchPluginXmlTask by tasks

publishPlugin {
    token(project.findProperty("INTELLIJ_TOKEN"))
}

patchPluginXml {
    changeNotes(
        """
        <ul>
            <li>1.0.3 Fix Intellij 2019
            <li>1.0.2 Fix XML Tag highlight.</li>
            <li>1.0.1 Annotate XML tag value & Resource declaration.</li>
            <li>1.0.0 first release</li>
        </ul>
        """
    )
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
