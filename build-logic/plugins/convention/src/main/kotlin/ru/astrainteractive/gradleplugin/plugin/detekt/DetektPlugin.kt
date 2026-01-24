package ru.astrainteractive.gradleplugin.plugin.detekt

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo

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
            val detektFileResource = Thread.currentThread()
                .getContextClassLoader()
                .getResource(detektYmlFileName)
            val bytes = detektFileResource
                ?.openConnection()
                ?.getInputStream()
                ?.readBytes()
                ?: byteArrayOf()

            val detektFile = target.rootProject.layout.buildDirectory.file(detektYmlFileName).get().asFile
            if (!detektFile.exists()) {
                detektFile.parentFile.mkdirs()
                detektFile.createNewFile()
            }
            detektFile.writeBytes(bytes)
            config.setFrom(detektFile)
            setSource(target.files(target.projectDir))

            include("**/*.kt", "**/*.kts")
            exclude(
                "**/resources/**",
                "**/build/**",
            )

            parallel = true

            buildUponDefaultConfig = true

            allRules = true

            // Target version of the generated JVM bytecode. It is used for type resolution.
            jvmTarget = target.requireJinfo.jtarget.majorVersion
        }

        target.dependencies {
            "detektPlugins"("com.braisgabin.detekt:kotlin-compiler-wrapper:0.0.4")
            "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
            if (useCompose) {
                "detektPlugins"("io.nlopez.compose.rules:detekt:0.5.3")
            }
        }
    }
}
