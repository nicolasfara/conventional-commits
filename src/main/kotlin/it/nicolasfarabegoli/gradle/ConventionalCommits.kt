package it.nicolasfarabegoli.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

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
        extension.setupScript()
    }
}
