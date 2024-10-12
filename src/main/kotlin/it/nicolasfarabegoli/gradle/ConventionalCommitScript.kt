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
    ignoreMessageCommit: String
) {
    project.logger.debug("Finding the '.git' folder")
    generateSequence(baseDir) { it.parentFile }.find { it.isGitFolder() }?.let {
        val scriptContent = createCommitMessage(types, scopes, successMessage, failureMessage, ignoreMessageCommit)
        val hooksFolder = it.resolve(".git/hooks/")
        if (!hooksFolder.exists()) hooksFolder.mkdir()
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
    ignoreMessageCommit: String
): String {
    val typesRegex = types.joinToString("|")
    val scopesRegex = if (scopes.isEmpty()) "[a-z \\-_]+" else scopes.joinToString("|")
    val successMessageEcho = wrapInEcho(successMessage)
    val failureMessageEcho = wrapInEcho(failureMessage)
    return """
        #!/usr/bin/env bash
        
        # Regex for conventional commits
        conventional_commits_regex="^($typesRegex)(\(($scopesRegex)\))?\!?:\ .+$"
        
        # Regex used to exclude message commit that match this regex
        exclude="$ignoreMessageCommit"
        
        # Get the commit message (the parameter we're given is just the path to the
        # temporary file which holds the message).
        commit_message=$(cat "$1")
        
        # Check if the message math the exclude regex, if so, all good baby.
        if [[ "${'$'}commit_message" =~ ${'$'}exclude ]]; then
            $successMessageEcho
            exit 0
        fi
        
        # Check the message, if we match, all good baby.
        if [[ "${'$'}commit_message" =~ ${'$'}conventional_commits_regex ]]; then
           $successMessageEcho
           exit 0
        fi
        
        # Uh-oh, this is not a conventional commit, show an example and link to the spec.
        $failureMessageEcho
        exit 1
    """.trimIndent()
}

private fun wrapInEcho(str: String?): String = str?.let { s -> "echo -e '${sanitize(s)}'" } ?: ""
private fun sanitize(str: String): String {
    val charsToEscape = listOf('\'')
    val escaped = escapeNewlines(str)
    return escapeChars(escaped, charsToEscape)
}

private fun escapeChar(c: Char, str: String): String = str.replace(c.toString(), "\\x%x".format(c.code))
private fun escapeChars(str: String, cs: List<Char>): String = cs.foldRight(str, ::escapeChar)
private fun escapeNewlines(str: String): String = str.replace("\r", "").replace("\n", "\\n").replace("'", "\\'")

private fun File.isGitFolder(): Boolean =
    listFiles()?.any { folder -> folder.isDirectory && folder.name == ".git" } ?: false

private fun File.writeScript(content: String) {
    writeText(content)
    setExecutable(true)
}
