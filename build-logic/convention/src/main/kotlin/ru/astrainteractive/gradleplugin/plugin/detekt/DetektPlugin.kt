package ru.astrainteractive.gradleplugin.plugin.detekt

import dev.detekt.gradle.Detekt
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerIfAbsent
import org.gradle.kotlin.dsl.withType
import ru.astrainteractive.gradleplugin.property.util.requireJinfo
import java.io.File

class DetektPlugin : Plugin<Project> {
    private fun readBundledConfig(): String {
        val stream = javaClass.classLoader.getResourceAsStream(DETEKT_CONFIG_FILE_NAME)
            ?: throw GradleException("$DETEKT_CONFIG_FILE_NAME is missing from the convention plugin jar")
        return stream.use { input -> input.readBytes().decodeToString() }
    }

    private fun Project.detektConfigFile(): File = isolated.rootProject
        .projectDirectory
        .file("build/detekt/$DETEKT_CONFIG_FILE_NAME")
        .asFile

    override fun apply(target: Project) {
        target.pluginManager.apply("dev.detekt")

        val configService = target.gradle.sharedServices.registerIfAbsent(
            name = CONFIG_SERVICE_NAME,
            implementationType = DetektConfigService::class,
            configureAction = {
                parameters.content.set(target.provider { readBundledConfig() })
                parameters.configFile.fileValue(target.detektConfigFile())
            }
        )
        val detektConfigFile = configService.map { service -> service.configFile }
        val jvmTargetVersion = target.requireJinfo.jtarget.majorVersion
        val projectSource = target.files(target.projectDir)

        target.tasks.register<Detekt>("detektFormat") {
            autoCorrect.set(true)
        }

        target.tasks.withType<Detekt>().configureEach {
            // Disable caching
            outputs.upToDateWhen { false }

            reports {
                html.required.set(true)
                checkstyle.required.set(false)
            }

            usesService(configService)
            config.setFrom(detektConfigFile)
            setSource(projectSource)

            include("**/*.kt", "**/*.kts")
            exclude(
                "**/resources/**",
                "**/build/**",
            )

            parallel.set(true)

            buildUponDefaultConfig.set(true)

            allRules.set(true)

            // Target version of the generated JVM bytecode. It is used for type resolution.
            jvmTarget.set(jvmTargetVersion)
        }

        target.dependencies {
            "detektPlugins"("dev.detekt:detekt-rules-ktlint-wrapper:2.0.0-alpha.2")
            "detektPlugins"("io.nlopez.compose.rules:detekt:0.5.6")
        }
    }

    private companion object {
        const val DETEKT_CONFIG_FILE_NAME = "detekt.yml"
        const val CONFIG_SERVICE_NAME = "klibsDetektConfig"
    }
}
