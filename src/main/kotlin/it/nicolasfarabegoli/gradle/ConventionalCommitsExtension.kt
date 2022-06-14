package it.nicolasfarabegoli.gradle

import org.gradle.api.Project

/**
 * Extension configuration.
 */
open class ConventionalCommitsExtension(private val project: Project) {

    var warningIfNoGitRoot: Boolean = true
    var types: List<String> = emptyList()
    var scopes: List<String> = emptyList()
    var successMessage: String? = null
    var failureMessage: String? = null

    /**
     * This method do all the work needed to write the script file.
     * Unfortunately, due to gradle workflow is the only way to apply the configuration without defining a manual task.
     */
    fun setupScript() {
        writeScript(project, project.projectDir, types, scopes, warningIfNoGitRoot, successMessage, failureMessage)
    }
}
