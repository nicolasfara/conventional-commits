package it.nicolasfarabegoli.gradle

private val types = listOf("build", "chore", "ci", "docs", "feat", "fix", "perf", "refactor", "revert", "style", "test")

fun buildRegex(listOfScopes: List<String> = emptyList()): Regex {
    return if (listOfScopes.isEmpty()) {
        "^(${types.joinToString("|")})(\\([a-z \\-]+\\))?!?: .+\$".toRegex()
    } else {
        "^(${types.joinToString("|")})\\((${listOfScopes.joinToString("|")})\\)?!?: .+\$".toRegex()
    }
}

val scriptContent = """
    #!/usr/bin/env bash

    # Create a regex for a conventional commit.
    conventional_commit_regex="{0}"

    # Get the commit message (the parameter we're given is just the path to the
    # temporary file which holds the message).
    commit_message=${'$'}(cat "${'$'}1")

    # Check the message, if we match, all good baby.
    if [[ "${'$'}commit_message" =~ ${'$'}conventional_commit_regex ]]; then
       echo -e "\e[32mCommit message meets Conventional Commit standards...\e[0m"
       exit 0
    fi

    # Uh-oh, this is not a conventional commit, show an example and link to the spec.
    echo -e "\e[31mThe commit message does not meet the Conventional Commit standard\e[0m"
    echo "An example of a valid message is: "
    echo "  feat(login): add the 'remember me' button"
    echo "More details at: https://www.conventionalcommits.org/en/v1.0.0/#summary"
    exit 1
""".trimIndent()
