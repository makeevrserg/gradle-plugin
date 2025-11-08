package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.androidLibrary
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.property.extension.AndroidModelPropertyValueExt.requireAndroidSdkInfo
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo
import ru.astrainteractive.gradleplugin.util.hasAndroidKmpPlugin
import ru.astrainteractive.gradleplugin.util.hasAndroidLibPlugin

class AndroidSdkPlugin : Plugin<Project> {

    private fun configureAndroidKmpPlugin(target: Project) {
        if (!target.hasAndroidKmpPlugin) return
        val androidSdkInfo = target.requireAndroidSdkInfo
        target.configure<KotlinMultiplatformExtension> {
            androidLibrary {
                compileSdk = androidSdkInfo.compile
                minSdk = androidSdkInfo.min
                // todo current version of Jetbrains Compose Resources
                // doesn't handle new android resourceless plugin
                androidResources.enable = true
            }
        }
    }

    private fun configureAndroidLibPlugin(target: Project) {
        if (!target.hasAndroidLibPlugin) return
        val jinfo = target.requireJinfo
        val androidSdkInfo = target.requireAndroidSdkInfo
        target.extensions.configure<BaseExtension> {
            compileSdkVersion(androidSdkInfo.compile)

            defaultConfig {
                minSdk = androidSdkInfo.min
                targetSdk = androidSdkInfo.target
            }
            compileOptions {
                sourceCompatibility = jinfo.jsource
                targetCompatibility = jinfo.jtarget
            }
        }
    }

    override fun apply(target: Project) {
        configureAndroidLibPlugin(target)
        configureAndroidKmpPlugin(target)
    }
}
