package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.hierarchyGroup
import ru.astrainteractive.gradleplugin.util.hasAndroidKmpPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidLibPlugin

class AndroidNamespacePlugin : Plugin<Project> {
    private fun configureAndroidKmpPlugin(target: Project) {
        if (!target.hasAndroidKmpPlugin) return
        target.configure<KotlinMultiplatformAndroidLibraryTarget> {
            namespace = target.hierarchyGroup
        }
    }

    private fun configureAndroidLibPlugin(target: Project) {
        if (!target.hasAndroidLibPlugin) return
        target.extensions.findByType<CommonExtension>()?.apply {
            this.namespace = target.hierarchyGroup
        }
    }

    override fun apply(target: Project) {
        configureAndroidLibPlugin(target)
        configureAndroidKmpPlugin(target)
    }
}
