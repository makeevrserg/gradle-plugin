package ru.astrainteractive.gradleplugin.detekt

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import ru.astrainteractive.gradleplugin.util.ProjectProperties.jinfo
import java.io.File

class DetektPlugin(private val useCompose: Boolean) : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("io.gitlab.arturbosch.detekt")
        }
        target.tasks.register<Detekt>("detektFormat") {
            autoCorrect = true
        }
        val detektYmlFileName = when {
            useCompose -> "detekt-compose.yml"
            else -> "detekt.yml"
        }

        target.tasks.withType<Detekt> {
            // Disable caching
            outputs.upToDateWhen { false }

            reports {
                html.required.set(true)
                xml.required.set(false)
                txt.required.set(false)
            }
            val resource = Thread.currentThread().getContextClassLoader().getResource(detektYmlFileName)
            val bytes = resource.openConnection().getInputStream().readAllBytes()
            val detektFile = File(File(target.rootDir, "build"), detektYmlFileName).also {
                if (!it.exists()) {
                    it.parentFile.mkdirs()
                    it.createNewFile()
                }
                it.writeBytes(bytes)
            }
            config.setFrom(detektFile)
            setSource(target.files(target.projectDir))

            include("**/*.kt", "**/*.kts")
            exclude(
                "**/resources/**",
                "**/build/**",
            )

            parallel = true

            buildUponDefaultConfig = true

            // Target version of the generated JVM bytecode. It is used for type resolution.
            jvmTarget = target.jinfo.ktarget.majorVersion
        }

        target.dependencies {
            "detektPlugins"("com.braisgabin.detekt:kotlin-compiler-wrapper:0.0.2")
            "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
            if (useCompose) {
                "detektPlugins"("com.twitter.compose.rules:detekt:0.0.26")
            }
        }
    }
}
