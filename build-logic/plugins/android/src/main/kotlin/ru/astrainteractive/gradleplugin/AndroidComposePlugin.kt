package ru.astrainteractive.gradleplugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.util.GradleProperty.Companion.gradleProperty

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configure<BaseExtension> {
            buildFeatures.compose = true
            composeOptions.kotlinCompilerExtensionVersion = target.gradleProperty(
                path = "android.kotlinCompilerExtensionVersion"
            ).string
        }
    }
}
