package it.nicolasfarabegoli.gradle

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty

open class ConventionalCommitsExtension(project: Project) {

    /**
     * List of scopes admitted in the project.
     * If an empty list is given, all scopes are admitted.
     * By default, any scope is admitted.
     */
    val scopes: ListProperty<String> = project.objects.listProperty()
}
