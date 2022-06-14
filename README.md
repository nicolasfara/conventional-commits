# conventional-commits

[![CI](https://github.com/nicolasfara/conventional-commits/actions/workflows/build-release.yml/badge.svg)](https://github.com/nicolasfara/conventional-commits/actions/workflows/build-release.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/it.nicolasfarabegoli/conventional-commits/badge.svg)](https://maven-badges.herokuapp.com/maven-central/it.nicolasfarabegoli/conventional-commits)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventional_commits-e10098?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

## Usage
```kotlin
plugins {
    id("it.nicolasfarabegoli.conventional-commits") version "<last-version>"
}
```

### Configuration

By default, no configuration is needed and a default script is generated.  
This script is **NOT** compatible with groovy syntax.  
For custom script, you could use mainly two method:

Provide the script directly:

```kotlin
conventionalCommits {
    from {
        "echo 'hello world'"
    }
    setupScript() //this is needed (at the very bottom) because of gradle :(
}
```

Or give a URL pointing to a script:

```kotlin
conventionalCommits {
    from(URL("https://example.com/commit-msg.sh"))
    
    setupScript() //this is needed (at the very bottom) because of gradle :(
}
```

By default, the first argument of the method `from` use the following shebang line: `#!/usr/bin/env bash`.
That shebang line could be overwritten passing the desired one, for example:

```kotlin
conventionalCommits {
    from("#!/bin/zsh") {
        "echo 'hello world'"
    }
    
    setupScript()
}
```
the previous configuration produce the following script:

```shell
#!/bin/zsh

echo 'hello world'
```