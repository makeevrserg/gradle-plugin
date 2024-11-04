package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.astrainteractive.gradleplugin.property.PropertyValue
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireInt
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString

object ExtendedPropertyValueExt {
    // JavaVersion
    val PropertyValue.javaVersion: Result<JavaVersion>
        get() = value.mapCatching { JavaVersion.toVersion(requireInt) }

    val PropertyValue.jvmTarget: Result<JvmTarget>
        get() = value.mapCatching { JvmTarget.fromTarget(requireString) }

    val PropertyValue.requireJavaVersion: JavaVersion
        get() = javaVersion.getOrThrow()

    val PropertyValue.requireJvmTarget: JvmTarget
        get() = jvmTarget.getOrThrow()
}
