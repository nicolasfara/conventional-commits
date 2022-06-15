package it.nicolasfarabegoli.gradle

import java.io.File

/**
 * Utility function for writing the content of `build.gradle.kts` with a string provider.
 */
fun File.configureBuildGradle(content: () -> String) = this.resolve("build.gradle.kts").writeText(content())

/**
 * Utility function for writing the content of `settings.gradle.kts` with a string provider.
 */
fun File.configureSettingsGradle(content: () -> String) = this.resolve("settings.gradle.kts").writeText(content())
