package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.hierarchyGroup
import ru.astrainteractive.gradleplugin.util.hasAndroidPlugin

class AndroidNamespacePlugin : Plugin<Project> {
    private fun configureAndroidKmpPlugin(target: Project) {
        target.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            target.configure<KotlinMultiplatformExtension> {
                targets.withType<KotlinMultiplatformAndroidLibraryTarget> {
                    namespace = target.hierarchyGroup
                }
            }
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
