package ru.astrainteractive.gradleplugin.property.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.PropertyValue
import java.io.File
import java.util.Properties

/**
 * This class will load property from local.properties project file
 */
class SecretPropertyValue(
    private val project: Project,
    override val key: String
) : PropertyValue {

    private fun Project.fileOrParentFile(name: String): File? {
        val projectFile = file(name).takeIf(File::exists)
        if (projectFile != null) return projectFile
        return parent?.fileOrParentFile(name)
    }

    private val localPropertiesFile: File
        get() = project.fileOrParentFile("local.properties")
            ?: project.rootProject.file("local.properties").takeIf(File::exists)
            ?: throw GradleException("No local.properties file found")

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
        val properties = Properties().apply {
            val secretPropsFile = localPropertiesFile
            load(secretPropsFile.reader())
        }
        return@runCatching properties.getProperty(key)
            ?: throw GradleException("Required property $key not defined!")
    }

    override fun isExist() = getValue().isSuccess
}
