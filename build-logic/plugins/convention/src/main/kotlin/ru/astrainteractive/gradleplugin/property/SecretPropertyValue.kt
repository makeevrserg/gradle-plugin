package ru.astrainteractive.gradleplugin.property

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.util.Properties

/**
 * This class will load property from local.properties project file
 */
class SecretPropertyValue(
    private val project: Project,
    override val key: String
) : PropertyValue {
    private val localPropertiesFile: File
        get() = project.file("local.properties").takeIf(File::exists)
            ?: project.rootProject.file("local.properties").takeIf(File::exists)
            ?: throw GradleException("No local.properties file found")

    override val value: Result<String>
        get() = kotlin.runCatching {
            // try to get system ci property
            val systemEnvProperty = System.getenv(key)
            if (systemEnvProperty != null) return@runCatching systemEnvProperty.toString()
            project.logger.warn("System.enviroment $key is missing. Getting it from local.properties")
            // if not ci getting from local.properties
            val properties = Properties().apply {
                val secretPropsFile = localPropertiesFile
                load(secretPropsFile.reader())
            }
            return@runCatching properties.getProperty(key)?.toString()
                ?: throw GradleException("Required property $key not defined!")
        }
    override val isExists: Boolean
        get() = value.isSuccess
}
