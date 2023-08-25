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
                projectDirectory.resolve(".git/hooks/commit-msg")
                    .readText() shouldContain "Commit message meets Conventional Commit standards..."
                // Test if `'` character is correctly escaped into `\x27`
                projectDirectory.resolve(".git/hooks/commit-msg")
                    .readText() shouldContain "feat(login): add the \\\\x27remember me\\\\x27 button"
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
        "not found the .git root" should {
            "generate a warning (by default) a no script file is created" {
                val projectDirectory = tempdir()
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
                tasks.output.contains("Not '.git' root found! No script will be generated") shouldBe true
            }
            "no generate a warning if the warning flag is set to false" {
                val projectDirectory = tempdir()
                projectDirectory.configureSettingsGradle { "" }
                projectDirectory.configureBuildGradle {
                    """
                        plugins {
                            id("it.nicolasfarabegoli.conventional-commits")
                        }
                        
                        conventionalCommits {
                            warningIfNoGitRoot = false
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
                tasks.output.contains("Not '.git' root found! No script will be generated") shouldBe false
            }
            "ignore the messages that match the given regex" {
                val projectDirectory = tempdir()
                projectDirectory.resolve(".git/hooks").mkdirs()
                projectDirectory.configureSettingsGradle { "" }
                projectDirectory.configureBuildGradle {
                    """
                        plugins {
                            id("it.nicolasfarabegoli.conventional-commits")
                        }
                        
                        conventionalCommits {
                            ignoreCommitMessage = "^Merge .+$"
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
                val scriptContent = projectDirectory.resolve(".git/hooks/commit-msg").readText()
                println(scriptContent)
                scriptContent shouldContain "^Merge .+\$"
            }
            "create the hook file even if the hooks folder do not exist (issue #170)" {
                val projectDirectory = tempdir()
                projectDirectory.resolve(".git/").mkdirs()
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
                projectDirectory.resolve(".git/hooks/commit-msg")
                    .readText() shouldContain "Commit message meets Conventional Commit standards..."
                // Test if `'` character is correctly escaped into `\x27`
                projectDirectory.resolve(".git/hooks/commit-msg")
                    .readText() shouldContain "feat(login): add the \\\\x27remember me\\\\x27 button"
            }
        }
    }
})
