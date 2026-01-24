package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.hierarchyGroup
import ru.astrainteractive.gradleplugin.util.hasAndroidAppPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidKmpPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidLibPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidPlugin

class AndroidNamespacePlugin : Plugin<Project> {
    private fun configureAndroidKmpPlugin(target: Project) {
        if (!target.hasAndroidKmpPlugin) return
        target.configure<KotlinMultiplatformAndroidLibraryExtension> {
            namespace = target.hierarchyGroup
        }
    }

    private fun configureAndroidPlugin(target: Project) {
        if (!target.hasAndroidPlugin) return
        target.extensions.configure<CommonExtension> {
            this.namespace = target.hierarchyGroup
        }
    }

    override fun apply(target: Project) {
        configureAndroidPlugin(target)
        configureAndroidKmpPlugin(target)
    }
}
