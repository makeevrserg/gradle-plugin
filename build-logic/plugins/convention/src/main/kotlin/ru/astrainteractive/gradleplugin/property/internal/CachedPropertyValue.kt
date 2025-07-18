package ru.astrainteractive.gradleplugin.property.internal

import org.gradle.api.plugins.ExtensionContainer
import ru.astrainteractive.gradleplugin.property.PropertyValue

class CachedPropertyValue(
    private val extensionContainer: ExtensionContainer,
    private val propertyValue: PropertyValue
) : PropertyValue {
    override val key: String = propertyValue.key

    override fun isExist(): Boolean = getValue().isSuccess

    private object EmptyValue
    private class PropertyValueNotPresentException : RuntimeException("Value not found")

    override fun getValue(): Result<String> {
        val extensionValue = extensionContainer.findByName(key)
        if (extensionValue == EmptyValue) {
            return Result.failure(PropertyValueNotPresentException())
        }
        if (extensionValue != null) return Result.success(extensionValue.toString())
        return propertyValue.getValue()
            .onSuccess { value -> extensionContainer.add(key, value) }
            .onFailure { extensionContainer.add(key, EmptyValue) }
    }
}
