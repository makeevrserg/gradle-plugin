package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.JavaVersion
import ru.astrainteractive.gradleplugin.property.PropertyValue
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireInt

object ExtendedPropertyValueExt {
    // JavaVersion
    val PropertyValue.javaVersion: Result<JavaVersion>
        get() = value.mapCatching { JavaVersion.toVersion(requireInt) }

    val PropertyValue.requireJavaVersion: JavaVersion
        get() = javaVersion.getOrThrow()
}
