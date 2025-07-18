package ru.astrainteractive.gradleplugin.property

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import ru.astrainteractive.gradleplugin.property.internal.CachedPropertyValue
import ru.astrainteractive.gradleplugin.property.internal.ProjectPropertyValue
import ru.astrainteractive.gradleplugin.property.internal.SecretPropertyValue

/**
 * This interface will load [String] value from property file
 */
interface PropertyValue {
    val key: String
    fun isExist(): Boolean
    fun getValue(): Result<String>
}

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

// Don't replace extensionContainer with project
// Different projects have different extensionContainer
fun PropertyValue.asCached(extensionContainer: ExtensionContainer): PropertyValue {
    return CachedPropertyValue(
        extensionContainer = extensionContainer,
        propertyValue = this
    )
}
