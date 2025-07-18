package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.model.AndroidSdkInfo
import ru.astrainteractive.gradleplugin.property.baseGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireInt

object AndroidModelPropertyValueExt {
    val Project.requireAndroidSdkInfo: AndroidSdkInfo
        get() = AndroidSdkInfo(
            compile = baseGradleProperty("android.sdk.compile").requireInt,
            min = baseGradleProperty("android.sdk.min").requireInt,
            target = baseGradleProperty("android.sdk.target").requireInt
        )
}
