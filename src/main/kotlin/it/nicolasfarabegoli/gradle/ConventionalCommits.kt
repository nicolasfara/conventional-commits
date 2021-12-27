package it.nicolasfarabegoli.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.text.MessageFormat

/**
 * Plugin for checking if commits are _Conventional Commits_ compliant.
 * This plugin can be customized base on the need of the user.
 * See [ConventionalCommitsExtension] for alla available config.
 * For more info on _Conventional Commits_ see [link](https://www.conventionalcommits.org/en/v1.0.0/).
 */
class ConventionalCommits : Plugin<Project> {
    companion object {
        private inline fun <reified T> Project.createExtension(name: String, vararg args: Any?): T =
            project.extensions.create(name, T::class.java, *args)
    }

    override fun apply(project: Project) {
        val extension = project.createExtension<ConventionalCommitsExtension>("conventionalCommits", project)
        val regex = buildRegex(extension.scopes.get())
        project.logger.debug("Use the following regex for check commits: $regex")

        val scriptPath = File(project.rootDir.path + File.separator + ".git" + File.separator, "hooks")
        if (!scriptPath.exists()) scriptPath.mkdirs()
        val scriptContent = MessageFormat.format(scriptContent, regex)
        project.logger.debug("script content: $scriptContent")
        scriptPath.resolve("commit-msg").writeText(scriptContent)
    }
}
