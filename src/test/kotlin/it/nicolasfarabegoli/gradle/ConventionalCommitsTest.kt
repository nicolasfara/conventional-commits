package it.nicolasfarabegoli.gradle

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

/**
 * Test class for the plugin.
 */
class ConventionalCommitsTest : WordSpec({
    val projectDir = File("build/gradleTest")
    fun setupTest() {
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle.kts").writeText("")
        projectDir.resolve("build.gradle.kts").writeText(
            """
            plugins {
                id("it.nicolasfarabegoli.conventional-commits")
            }
            """.trimIndent()
        )
    }

    "The plugin" should {
        "create a script file" {
            setupTest()
            val tasks = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(projectDir)
                .withArguments("tasks")
                .build()
            tasks.task(":tasks")?.outcome shouldBe TaskOutcome.SUCCESS
        }
    }
})
