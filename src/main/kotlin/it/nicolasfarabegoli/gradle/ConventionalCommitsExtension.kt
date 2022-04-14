package it.nicolasfarabegoli.gradle

import org.gradle.api.Project
import java.io.File
import java.net.URL

/**
 * Extension configuration.
 */
open class ConventionalCommitsExtension(private val project: Project) {

    companion object {
        private const val DEFAULT_SCRIPT_PATH = "it/nicolasfarabegoli/gradle/commit-msg.sh"

        private fun File.isGitFolder(): Boolean =
            listFiles()?.any { folder -> folder.isDirectory && folder.name == ".git" } ?: false

        private fun File.writeScript(content: String) {
            writeText(content)
            setExecutable(true)
        }
    }

    private var script: String? = null

    /**
     * Path where the script is write.
     * By default a git root is searched and if found is used to put the script in `.git/hooks/commit-msg`.
     */
    val scriptPath: File? = null
        get() =
            field ?: requireNotNull(generateSequence(project.projectDir) { it.parentFile }.find { it.isGitFolder() }) {
                "Unable to find the `.git` folder inside the project from ${project.projectDir.absolutePath}"
            }

    /**
     * This method enable a user'script definition.
     * By default the [shebang] is `#!/usr/bin/env bash`. With [scriptProducer] is possible to write the custom script.
     */
    fun from(shebang: String? = "#!/usr/bin/env bash", scriptProducer: () -> String) {
        val header = shebang?.takeIf { it.isNotBlank() }?.let { "$it\n\n" } ?: ""
        script = header + scriptProducer()
    }

    /**
     * Generate the script from the given [url].
     */
    fun from(url: URL): Unit = from("") { url.readText() }

    /**
     * This method do all the work needed to write the script file.
     * Unfortunately, due to gradle workflow is the only way to apply the configuration without defining a manual task.
     */
    fun setupScript() {
        project.logger.debug("")
        val content = script ?: defaultScript()
        serializeToFile("commit-msg", content)
    }

    private fun serializeToFile(fileName: String, content: String) {
        val writeToFile = File(scriptPath?.absolutePath, ".git/hooks/$fileName")
        writeToFile.writeScript(content)
    }

    private fun defaultScript(): String {
        val defaultScriptPath = Thread.currentThread().contextClassLoader.getResource(DEFAULT_SCRIPT_PATH)
        requireNotNull(defaultScriptPath) {
            """
                Unable to find the script path at: $DEFAULT_SCRIPT_PATH
                
                That could be a bug in the class plugin ${ConventionalCommits::class.simpleName}
            """.trimIndent()
        }
        return defaultScriptPath.readText()
    }
}
