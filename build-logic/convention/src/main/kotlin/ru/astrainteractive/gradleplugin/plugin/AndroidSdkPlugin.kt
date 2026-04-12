package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.property.util.requireAndroidSdkInfo
import ru.astrainteractive.gradleplugin.util.hasAndroidAppPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidKmpPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidLibPlugin

class AndroidSdkPlugin : Plugin<Project> {

    private fun configureAndroidKmpPlugin(target: Project) {
        if (!target.hasAndroidKmpPlugin) return
        val androidSdkInfo = target.requireAndroidSdkInfo
        target.pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            target.configure<KotlinMultiplatformExtension> {
                targets.withType<KotlinMultiplatformAndroidLibraryTarget> {
                    compileSdk = androidSdkInfo.compile
                    minSdk = androidSdkInfo.min
                    // todo current version of Jetbrains Compose Resources
                    // todo doesn't handle new android resourceless plugin
                    androidResources.enable = true
                }
            }
        }
    }

    private fun configureAndroidLibraryExtension(target: Project) {
        if (!target.hasAndroidLibPlugin) return
        val androidSdkInfo = target.requireAndroidSdkInfo
        target.extensions.configure<LibraryExtension> {
            compileSdk = androidSdkInfo.compile

            defaultConfig {
                minSdk = androidSdkInfo.min
            }
        }
    }

    private fun configureAndroidApplicationExtension(target: Project) {
        if (!target.hasAndroidAppPlugin) return
        val androidSdkInfo = target.requireAndroidSdkInfo
        target.extensions.configure<ApplicationExtension> {
            compileSdk = androidSdkInfo.compile

            defaultConfig {
                minSdk = androidSdkInfo.min
                targetSdk = androidSdkInfo.target
            }
        }
    }

    override fun apply(target: Project) {
        configureAndroidLibraryExtension(target)
        configureAndroidApplicationExtension(target)
        configureAndroidKmpPlugin(target)
    }
}
