package it.nicolasfarabegoli.gradle

import org.gradle.api.Project
import java.io.File

internal fun writeScript(
    project: Project,
    baseDir: File,
    types: List<String>,
    scopes: List<String>,
    warningIfNoGitRoot: Boolean,
    successMessage: String?,
    failureMessage: String?,
) {
    project.logger.debug("Finding the '.git' folder")
    generateSequence(baseDir) { it.parentFile }.find { it.isGitFolder() }?.let {
        val scriptContent = createCommitMessage(types, scopes, successMessage, failureMessage)
        it.resolve(".git/hooks/commit-msg").writeScript(scriptContent)
        project.logger.debug("[ConventionalCommits] script file written in '.git/hooks/commit-msg'")
    } ?: run {
        project.logger.debug("[ConventionalCommits] '.git' folder not found. No action taken")
        if (warningIfNoGitRoot) {
            project.logger.warn("[ConventionalCommits] Not '.git' root found! No script will be generated")
        }
    }
}

private fun createCommitMessage(
    types: List<String>,
    scopes: List<String>,
    successMessage: String?,
    failureMessage: String?,
): String {
    val typesRegex = types.joinToString("|")
    val scopesRegex = if (scopes.isEmpty()) "[a-z \\-]+" else scopes.joinToString("|")
    val successMessageEcho = wrapInEcho(successMessage)
    val failureMessageEcho = wrapInEcho(failureMessage)
    return """
        #!/usr/bin/env bash
        
        # Regex for conventional commits
        conventional_commits_regex="^($typesRegex)(\\($scopesRegex))?!?: .+$
        
        # Get the commit message (the parameter we're given is just the path to the
        # temporary file which holds the message).
        commit_message=$(cat "$1")
        
        # Check the message, if we match, all good baby.
        if [[ "${'$'}commit_message" =~ ${'$'}conventional_commit_regex ]]; then
           $successMessageEcho
           exit 0
        fi
        
        # Uh-oh, this is not a conventional commit, show an example and link to the spec.
        $failureMessageEcho
        exit 1
    """.trimIndent()
}

private fun wrapInEcho(str: String?): String = str?.let { s -> "echo -e '${escape(s)}'" } ?: ""
private fun escape(str: String): String = str.replace("\r", "").replace("\n", "\\n").replace("'", "\\'")

private fun File.isGitFolder(): Boolean =
    listFiles()?.any { folder -> folder.isDirectory && folder.name == ".git" } ?: false

private fun File.writeScript(content: String) {
    writeText(content)
    setExecutable(true)
}
