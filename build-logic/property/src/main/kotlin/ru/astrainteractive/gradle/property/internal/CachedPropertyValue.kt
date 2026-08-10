package ru.astrainteractive.gradle.property.internal

import ru.astrainteractive.gradle.property.api.PropertyValue

internal class CachedPropertyValue(
    private val cache: BuildPropertyCacheService,
    private val propertyValue: PropertyValue
) : PropertyValue {
    override val key: String = propertyValue.key

    private class PropertyValueNotPresentException : RuntimeException("Value not found")

    override fun getValue(): Result<String> {
        var resolvedHere = false
        val value = cache.computeIfAbsent(key) {
            resolvedHere = true
            propertyValue.getValue()
        }
        return when {
            resolvedHere || value.isSuccess -> value
            else -> Result.failure(PropertyValueNotPresentException())
        }
    }
}
