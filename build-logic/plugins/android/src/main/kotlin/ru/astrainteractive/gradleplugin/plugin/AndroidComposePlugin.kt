package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.baseGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.stringOrNull

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val compilerVersion = target
            .baseGradleProperty(path = "android.kotlinCompilerExtensionVersion")
            .stringOrNull

        target.extensions.configure<CommonExtension> {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = compilerVersion
        }
    }
}
