package it.nicolasfarabegoli.gradle

import java.io.File

fun File.configureBuildGradle(content: () -> String) = this.resolve("build.gradle.kts").writeText(content())

fun File.configureSettingsGradle(content: () -> String) = this.resolve("settings.gradle.kts").writeText(content())
