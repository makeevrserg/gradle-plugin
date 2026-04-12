package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradle.property.api.klibsGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireInt
import ru.astrainteractive.gradleplugin.property.model.AndroidSdkInfo

object AndroidModelPropertyValueExt {
    val Project.requireAndroidSdkInfo: AndroidSdkInfo
        get() = AndroidSdkInfo(
            compile = klibsGradleProperty("android.sdk.compile").requireInt,
            min = klibsGradleProperty("android.sdk.min").requireInt,
            target = klibsGradleProperty("android.sdk.target").requireInt
        )
}
