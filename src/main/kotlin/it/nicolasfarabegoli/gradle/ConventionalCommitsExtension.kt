package it.nicolasfarabegoli.gradle

import org.gradle.api.Project

/**
 * Extension configuration.
 */
open class ConventionalCommitsExtension(private val project: Project) {

    /**
     * If set to _true_ a warning is raised if no _.git_ folder is found.
     */
    var warningIfNoGitRoot: Boolean = true

    /**
     * List of admitted types.
     *
     * Default values are: _build, chore, ci, docs, feat, fix, perf, refactor, revert, style, test_.
     */
    var types: List<String> = listOf(
        "build",
        "chore",
        "ci",
        "docs",
        "feat",
        "fix",
        "perf",
        "refactor",
        "revert",
        "style",
        "test",
    )

    /**
     * List of admitted scopes.
     *
     * By default, an empty list is set, meaning that all scopes are admitted.
     */
    var scopes: List<String> = emptyList()

    /**
     * The message that will be shown if the commit is conventional commit compliant.
     */
    var successMessage: String? = "\\e[32mCommit message meets Conventional Commit standards...\\e[0m"

    /**
     * The message that will be shown if the commit is not conventional commit compliant.
     */
    var failureMessage: String? = """
        \e[31mThe commit message does not meet the Conventional Commit standard\e[0m
        An example of a valid message is: 
          feat(login): add the 'remember me' button
        More details at: https://www.conventionalcommits.org/en/v1.0.0/#summary
    """.trimIndent()

    /**
     * If the commit message match the following regex, the message is excluded from the check.
     */
    var ignoreCommitMessage: String = ".^"

    /**
     * This method do all the work needed to write the script file.
     */
    internal fun setupScript() {
        writeScript(
            project,
            project.projectDir,
            types,
            scopes,
            warningIfNoGitRoot,
            successMessage,
            failureMessage,
            ignoreCommitMessage
        )
    }
}
