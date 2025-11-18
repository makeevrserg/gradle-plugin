package ru.astrainteractive.gradleplugin.property.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.PropertyValue
import ru.astrainteractive.gradleplugin.property.gradleProperties
import ru.astrainteractive.gradleplugin.property.localProperties

/**
 * This class will load property from gradle.properties project file
 */
class ProjectPropertyValue(
    private val project: Project,
    override val key: String
) : PropertyValue {
    private fun getGradleProperty(): Result<String> {
        return runCatching {
            project.providers.gradleProperty(key)
                .orNull
                ?: throw GradleException("Required property $key not defined!")
        }.onFailure { project.logger.warn("Couldn't find $key with gradle's project.providers.gradleProperty") }
    }

    private fun getGradleFileProperty(): Result<String> {
        return runCatching { project.gradleProperties.getProperty(key) }
            .onFailure { project.logger.warn("Couldn't find $key in gradle.properties") }
    }

    private fun getLocalProperty(): Result<String> {
        return runCatching { project.localProperties.getProperty(key) }
            .onFailure { project.logger.warn("Couldn't find $key in local.properties") }
    }

    override fun getValue() = runCatching {
        getLocalProperty().getOrNull()
            ?: getGradleProperty().getOrNull()
            ?: getGradleFileProperty().getOrThrow()
    }
}
