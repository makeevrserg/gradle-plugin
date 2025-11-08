package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.androidLibrary
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.hierarchyGroup
import ru.astrainteractive.gradleplugin.util.hasAndroidKmpPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidLibPlugin

class AndroidNamespacePlugin : Plugin<Project> {
    private fun configureAndroidKmpPlugin(target: Project) {
        if (!target.hasAndroidKmpPlugin) return
        target.configure<KotlinMultiplatformExtension> {
            androidLibrary {
                namespace = target.hierarchyGroup
            }
        }
    }

    private fun configureAndroidLibPlugin(target: Project) {
        if (!target.hasAndroidLibPlugin) return
        target.configure<BaseExtension> {
            namespace = target.hierarchyGroup
        }
    }

    override fun apply(target: Project) {
        configureAndroidKmpPlugin(target)
        configureAndroidLibPlugin(target)
    }
}
