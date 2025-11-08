package ru.astrainteractive.gradleplugin.property.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.PropertyValue
import ru.astrainteractive.gradleplugin.property.fileOrParentFile
import ru.astrainteractive.gradleplugin.property.fromFile
import java.util.Properties

/**
 * This class will load property from gradle.properties project file
 */
class ProjectPropertyValue(
    private val project: Project,
    override val key: String
) : PropertyValue {
    private fun getGradleProperty(): Result<String> {
        project.logger.warn("#getGradleProperty $key")
        return runCatching {
            project.property(key)
                ?.toString()
                ?: throw GradleException("Required property $key not defined!")
        }
    }

    private fun getFileProperty(): Result<String> {
        project.logger.warn("#getFileProperty $key")
        return runCatching {
            val file = project
                .fileOrParentFile("gradle.properties")
                ?: throw GradleException("No gradle.properties found in project")
            Properties()
                .fromFile(file)
                .getProperty(key)
                ?: throw GradleException("Required property $key not found in gradle.properties!")
        }
    }

    override fun getValue() = runCatching {
        getGradleProperty()
            .onFailure { project.logger.warn("Couldn't find $key with gradle's project.property()") }
            .getOrNull()
            ?: getFileProperty().getOrThrow()
    }

    override fun isExist() = project.hasProperty(key)
}
