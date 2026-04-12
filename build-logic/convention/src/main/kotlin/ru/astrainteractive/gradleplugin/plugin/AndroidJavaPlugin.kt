package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.property.util.requireJinfo
import ru.astrainteractive.gradleplugin.util.hasAndroidPlugin

class AndroidJavaPlugin : Plugin<Project> {
    private fun configureAndroidKmpPlugin(target: Project) {
        target.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            target.extensions.configure<KotlinMultiplatformExtension> {
                targets
                    .filterIsInstance<HasConfigurableKotlinCompilerOptions<*>>()
                    .mapNotNull { target -> target.compilerOptions as? KotlinJvmCompilerOptions }
                    .onEach { androidTarget ->
                        androidTarget.jvmTarget.set(JvmTarget.fromTarget(target.requireJinfo.jtarget.majorVersion))
                    }
            }
        }
    }

    private fun configureAndroidPlugin(target: Project) {
        if (!target.hasAndroidPlugin) return
        target.extensions.configure<CommonExtension> {
            compileOptions.sourceCompatibility = target.requireJinfo.jsource
            compileOptions.targetCompatibility = target.requireJinfo.jtarget
        }
    }

    override fun apply(target: Project) {
        configureAndroidPlugin(target)
        configureAndroidKmpPlugin(target)
    }
}
