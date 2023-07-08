package ru.astrainteractive.gradleplugin

import ru.astrainteractive.gradleplugin.util.GradleProperty.Companion.gradleProperty
import io.gitlab.arturbosch.detekt.Detekt
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class CoreDetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("io.gitlab.arturbosch.detekt")
        }
        target.tasks.register<Detekt>("detektFormat") {
            autoCorrect = true
        }

        target.tasks.withType<Detekt> {
            // Disable caching
            outputs.upToDateWhen { false }

            reports {
                html.required.set(true)
                xml.required.set(false)
                txt.required.set(false)
            }
            val resource = Thread.currentThread().getContextClassLoader().getResource("detekt.yml")
            val bytes = resource.openConnection().getInputStream().readAllBytes()
            val detektFile = File(File(target.rootDir, "build"),"detekt.yml").also {
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
            jvmTarget = target.gradleProperty("java.ktarget").javaVersion.majorVersion
        }

        target.dependencies {
            "detektPlugins"("com.braisgabin.detekt:kotlin-compiler-wrapper:0.0.2")
            "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
        }

    }
}
