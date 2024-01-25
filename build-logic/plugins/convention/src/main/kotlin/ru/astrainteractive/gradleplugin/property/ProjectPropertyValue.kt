package ru.astrainteractive.gradleplugin.property

import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * This class will load property from gradle.properties project file
 */
class ProjectPropertyValue(private val project: Project, override val key: String) : PropertyValue {
    override val value: Result<String>
        get() = kotlin.runCatching {
            project.property(key)
                ?.toString()
                ?: throw GradleException("Required property $key not defined!")
        }
    override val isExists: Boolean
        get() = project.hasProperty(key)
}
