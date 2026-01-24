package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import ru.astrainteractive.gradleplugin.property.baseGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val compilerVersion = target
            .baseGradleProperty(path = "android.kotlinCompilerExtensionVersion")
            .requireString

        target.extensions.findByType<LibraryExtension>()?.apply {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = compilerVersion
        }

        target.extensions.findByType<ApplicationExtension>()?.apply {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = compilerVersion
        }
    }
}
