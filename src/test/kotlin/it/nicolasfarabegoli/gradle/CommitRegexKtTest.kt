package it.nicolasfarabegoli.gradle

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class CommitRegexKtTest : WordSpec({
    "A commit message" should {
        "admit any scope if no scopes are given" {
            buildRegex().matches("feat(scope): foo bar") shouldBe true
            buildRegex().matches("foo bar") shouldBe false
            buildRegex().matches("feat: foo bar") shouldBe true
        }
        "not admit scopes not allowed" {
            val scopes = listOf("scope1", "scope2", "scope3")
            buildRegex(scopes).matches("feat(scope1): foo bar") shouldBe true
            buildRegex(scopes).matches("feat: foo bar") shouldBe false
            buildRegex(scopes).matches("feat(scope4): foo bar") shouldBe false
        }
    }
})
