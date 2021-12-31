# conventional-commits

[![CI](https://github.com/nicolasfara/conventional-commits/actions/workflows/build-release.yml/badge.svg)](https://github.com/nicolasfara/conventional-commits/actions/workflows/build-release.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/it.nicolasfarabegoli/conventional-commits/badge.svg)](https://maven-badges.herokuapp.com/maven-central/it.nicolasfarabegoli/conventional-commits)

## Usage
```kotlin
plugins {
    id("it.nicolasfarabegoli.conventional-commits") version "<last-version>"
}
```

### Configuration

By default, no configuration is needed and the behaviour is that all scopes are allowed.  
For customizing the scopes, a list of String should be given, for example:

```kotlin
conventionalCommits {
    scopes.set(listOf("scope1", "scope2", "scope3"))
}
```