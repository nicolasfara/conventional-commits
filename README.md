# conventional-commits

[![CI](https://github.com/nicolasfara/conventional-commits/actions/workflows/build-release.yml/badge.svg)](https://github.com/nicolasfara/conventional-commits/actions/workflows/build-release.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/it.nicolasfarabegoli/conventional-commits/badge.svg)](https://maven-badges.herokuapp.com/maven-central/it.nicolasfarabegoli/conventional-commits)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventional_commits-e10098?logo=semantic-release)](https://github.com/semantic-release/semantic-release)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

The purpose of this plugin is to enforce the use of [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) in a Gradle-based project.

## Setup
```kotlin
plugins {
    id("it.nicolasfarabegoli.conventional-commits") version "<last-version>"
}
```

Simply applying the plugin as above, a pre-configured script for conventional commit check is generate.

The plugin creates a git hooks file that enforce that each commit is compliant with the Conventional Commit convention:

```bash
> git commit -m "some message"
The commit message does not meet the Conventional Commit standard
An example of a valid message is:
  feat(login): add the 'remember me' button
More details at: https://www.conventionalcommits.org/en/v1.0.0/#summary

> git commit -m "feat: we love conventional commit!"
Commit message meets Conventional Commit standards...
```

All the available plugin's keys are shown below:

```kotlin
conventionalCommits {
    warningIfNoGitRoot = true
    
    types += listOf("types", "type2") // Add those types to the standard ones
    
    scopes = emptyList()
    
    successMessage = "Commit message meets Conventional Commit standards..."
    
    failureMessage = "The commit message does not meet the Conventional Commit standard"
}
```

The following table describe all the available keys in the plugin

| Key                  | Description                                                                                                | Default                                                                                      |
|----------------------|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| `warningIfNoGitRoot` | A **warning** is raised if no `.git` root is found walking up until the `/` from the project folder.       | `true`                                                                                       |
| `types`              | List of admitted types in the commit message.                                                              | `build`, `chore`, `ci`, `docs`, `feat`, `fix`, `perf`, `refactor`, `revert`, `style`, `test` |
| `scopes`             | List of admitted scopes in the commit message. An empty list means that all scopes are admitted            | `emptyList`                                                                                  |
| `successMessage`     | A message printed if the commit meets conventional commit. If `null` is set, no message is printed.        | "Commit message meets Conventional Commit standards..."                                      |
| `failureMessage`     | A message printed if the commit **not** meets conventional commit. If `null` is set no message is printed. | "The commit message does not meet the Conventional Commit standard"                          |
