import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.publish.central)
    alias(libs.plugins.publish)
    alias(libs.plugins.conventional.commits)
}

group = "it.nicolasfarabegoli"
val projectId = "$group.$name"
val fullName = "Configure conventional commit"
val websiteUrl = "https://github.com/nicolasfara/conventional-commits"
val projectDetail = "Plugin to check if commits are 'Conventional Commits' compliant"
val pluginImplementationClass = "it.nicolasfarabegoli.gradle.ConventionalCommits"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(kotlin("stdlib"))
    api(gradleApi())
    api(gradleKotlinDsl())
    testImplementation(gradleTestKit())
    testImplementation(libs.bundles.kotest)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        allWarningsAsErrors = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}

pluginBundle {
    website = websiteUrl
    vcsUrl = websiteUrl
    tags = listOf("conventional-commits")
}

gradlePlugin {
    plugins {
        create("ConventionalCommits") {
            id = projectId
            displayName = fullName
            description = projectDetail
            implementationClass = pluginImplementationClass
        }
    }
}

publishOnMavenCentral {
    projectDescription.set(projectDetail)
    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    developers {
                        developer {
                            name.set("Nicolas Farabegoli")
                            email.set("nicolas.farabegoli@gmail.com")
                            url.set("https://github.com/nicolasfara")
                        }
                    }
                }
            }
        }
    }
}
