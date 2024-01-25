package ru.astrainteractive.gradleplugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.PropertyValue.Companion.baseGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configure<BaseExtension> {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = target.baseGradleProperty(
                path = "android.kotlinCompilerExtensionVersion"
            ).requireString
        }
    }
}
