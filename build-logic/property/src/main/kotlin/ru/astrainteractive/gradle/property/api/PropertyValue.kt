package ru.astrainteractive.gradle.property.api

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import ru.astrainteractive.gradle.property.internal.CachedPropertyValue
import ru.astrainteractive.gradle.property.internal.ProjectPropertyValue
import ru.astrainteractive.gradle.property.internal.SecretPropertyValue

/**
 * This interface will load [String] value from property file
 */
interface PropertyValue {
    val key: String
    fun getValue(): Result<String>
}

private const val BASE_PREFIX = "klibs"

fun Project.gradleProperty(path: String): PropertyValue {
    return ProjectPropertyValue(this, path)
}

fun Project.secretProperty(path: String): PropertyValue {
    return SecretPropertyValue(this, path)
}

fun Project.klibsGradleProperty(path: String): PropertyValue {
    return ProjectPropertyValue(this, "$BASE_PREFIX.$path")
}

fun Project.klibsSecretProperty(path: String): PropertyValue {
    return SecretPropertyValue(this, "$BASE_PREFIX.$path")
}

/**
 * Don't replace extensionContainer with project
 *
 * Different projects have different extensionContainer
 */
fun PropertyValue.asCached(extensionContainer: ExtensionContainer): PropertyValue {
    return CachedPropertyValue(
        extensionContainer = extensionContainer,
        propertyValue = this
    )
}
