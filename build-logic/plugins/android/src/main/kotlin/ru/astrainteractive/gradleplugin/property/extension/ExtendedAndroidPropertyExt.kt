package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.PropertyValue.Companion.baseGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireInt

object ExtendedAndroidPropertyExt {
    val Project.requireVersionCode: Int
        get() = baseGradleProperty("project.version.code").requireInt
}
