package ru.astrainteractive.gradle.property.api

import org.gradle.api.Project
import ru.astrainteractive.gradle.property.internal.BuildPropertyCacheService
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
private const val CACHE_SERVICE_NAME = "klibsBuildPropertyCache"

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
 * Memoizes this property for the whole build.
 *
 * Only for keys that must resolve to one value everywhere, per-module keys must stay uncached.
 */
fun PropertyValue.asCached(project: Project): PropertyValue {
    val cache = project.gradle.sharedServices
        .registerIfAbsent(CACHE_SERVICE_NAME, BuildPropertyCacheService::class.java) {}
        .get()
    return CachedPropertyValue(
        cache = cache,
        propertyValue = this
    )
}
