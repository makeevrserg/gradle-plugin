package ru.astrainteractive.gradleplugin.property.extension

import ru.astrainteractive.gradleplugin.property.PropertyValue

object PrimitivePropertyValueExt {
    // String
    val PropertyValue.stringOrNull: String?
        get() = getValue().getOrNull()
    val PropertyValue.stringOrEmpty: String
        get() = stringOrNull.orEmpty()
    val PropertyValue.requireString: String
        get() = getValue().getOrThrow()

    // Integer
    val PropertyValue.int: Result<Int>
        get() = getValue().mapCatching { it.toInt() }
    val PropertyValue.intOrNull: Int?
        get() = int.getOrNull()
    val PropertyValue.requireInt: Int
        get() = int.getOrThrow()
}
