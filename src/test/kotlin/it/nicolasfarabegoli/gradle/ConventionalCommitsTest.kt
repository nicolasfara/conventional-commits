package it.nicolasfarabegoli.gradle

import io.kotest.core.spec.style.WordSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

/**
 * Test class for the plugin.
 */
class ConventionalCommitsTest : WordSpec({
    "The plugin" `when` {
        "is applied without configuration" should {
            "write the default script" {
                val projectDirectory = tempdir()
                projectDirectory.resolve(".git/hooks").mkdirs()
                projectDirectory.configureSettingsGradle { "" }
                projectDirectory.configureBuildGradle {
                    """
                        plugins {
                            id("it.nicolasfarabegoli.conventional-commits")
                        }
                    """.trimIndent()
                }

                val tasks = GradleRunner.create()
                    .forwardOutput()
                    .withPluginClasspath()
                    .withProjectDir(projectDirectory)
                    .withArguments("tasks")
                    .build()
                tasks.task(":tasks")?.outcome shouldBe TaskOutcome.SUCCESS

                projectDirectory.resolve(".git/hooks/commit-msg").exists() shouldBe true
            }
        }

        "is configured with custom scopes and types" should {
            "admit only those scopes and type" {
                val projectDirectory = tempdir()
                projectDirectory.resolve(".git/hooks").mkdirs()
                projectDirectory.configureSettingsGradle { "" }
                projectDirectory.configureBuildGradle {
                    """
                        plugins {
                            id("it.nicolasfarabegoli.conventional-commits")
                        }
                        
                        conventionalCommits {
                            scopes = listOf("scope1", "scope2")
                            types += listOf("type1")
                            
                            setupScript()
                        }
                    """.trimIndent()
                }

                val tasks = GradleRunner.create()
                    .forwardOutput()
                    .withPluginClasspath()
                    .withProjectDir(projectDirectory)
                    .withArguments("tasks")
                    .build()
                tasks.task(":tasks")?.outcome shouldBe TaskOutcome.SUCCESS

                val scriptFile = projectDirectory.resolve(".git/hooks/commit-msg")
                scriptFile.exists() shouldBe true
                scriptFile.readText() shouldContain "scope1|scope2"
                scriptFile.readText() shouldContain "type1"
            }
        }
    }
})
