package ru.astrainteractive.gradleplugin.property.internal

import org.gradle.api.plugins.ExtensionContainer
import ru.astrainteractive.gradleplugin.property.PropertyValue

class CachedPropertyValue(
    private val extensionContainer: ExtensionContainer,
    private val propertyValue: PropertyValue
) : PropertyValue {
    override val key: String = propertyValue.key

    private object EmptyValue
    private class PropertyValueNotPresentException : RuntimeException("Value not found")

    override fun getValue(): Result<String> {
        val extensionValue = extensionContainer.findByName(key)
        return when {
            extensionValue == EmptyValue -> Result.failure(PropertyValueNotPresentException())
            extensionValue != null -> Result.success(extensionValue.toString())
            else -> propertyValue.getValue()
                .onSuccess { value -> extensionContainer.add(key, value) }
                .onFailure { extensionContainer.add(key, EmptyValue) }
        }
    }
}
