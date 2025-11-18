package ru.astrainteractive.gradleplugin.property.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.PropertyValue
import ru.astrainteractive.gradleplugin.property.localProperties

/**
 * This class will load property from local.properties project file
 */
class SecretPropertyValue(
    private val project: Project,
    override val key: String
) : PropertyValue {

    /**
     * System.getenv doesn't allow dots
     */
    private val envKey: String = key.replace(".", "_")

    override fun getValue() = runCatching {
        // try to get system ci property
        val systemEnvProperty = System.getenv(envKey)
        if (systemEnvProperty != null) return@runCatching systemEnvProperty
        project.logger.warn("System.enviroment $envKey is missing. Getting it from local.properties")
        // if not ci getting from local.properties
        return@runCatching project.localProperties
            .getProperty(key)
            ?: throw GradleException("Required property $key not defined!")
    }
}
