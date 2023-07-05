
import com.makeevrserg.gradleplugin.util.GradleProperty.Companion.gradleProperty
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

tasks.register<Detekt>("detektFormat") {
    autoCorrect = true
}

tasks.withType<Detekt> {
    // Disable caching
    outputs.upToDateWhen { false }

    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }

    setSource(files(projectDir))
    config.setFrom(files("$rootDir/gradle/detekt.yml"))

    include("**/*.kt", "**/*.kts")
    exclude(
        "**/resources/**",
        "**/build/**",
    )

    parallel = true

    buildUponDefaultConfig = true

    // Target version of the generated JVM bytecode. It is used for type resolution.
    jvmTarget = gradleProperty("java.ktarget").javaVersion.majorVersion
}

dependencies {
    detektPlugins(libs.lint.detekt.ruleset.compiler)
    detektPlugins(libs.lint.detekt.ruleset.ktlint)
    detektPlugins(libs.lint.detekt.ruleset.compose)
}
