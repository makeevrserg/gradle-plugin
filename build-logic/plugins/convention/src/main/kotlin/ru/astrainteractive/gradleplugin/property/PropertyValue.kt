package ru.astrainteractive.gradleplugin.property

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.internal.ProjectPropertyValue
import ru.astrainteractive.gradleplugin.property.internal.SecretPropertyValue

/**
 * This interface will load [String] value from property file
 */
interface PropertyValue {
    val key: String
    val value: Result<String>
    val isExists: Boolean

    companion object {
        private const val BASE_PREFIX = "makeevrserg"

        fun Project.gradleProperty(path: String): ProjectPropertyValue {
            return ProjectPropertyValue(this, path)
        }

        fun Project.secretProperty(path: String): SecretPropertyValue {
            return SecretPropertyValue(this, path)
        }

        fun Project.baseGradleProperty(path: String): ProjectPropertyValue {
            return ProjectPropertyValue(this, "$BASE_PREFIX.$path")
        }

        fun Project.baseSecretProperty(path: String): SecretPropertyValue {
            return SecretPropertyValue(this, "$BASE_PREFIX.$path")
        }
    }
}
