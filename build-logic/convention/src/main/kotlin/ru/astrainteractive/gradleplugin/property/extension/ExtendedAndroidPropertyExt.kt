package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradle.property.api.klibsGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireInt

object ExtendedAndroidPropertyExt {
    val Project.requireVersionCode: Int
        get() = klibsGradleProperty("project.version.code").requireInt
}
