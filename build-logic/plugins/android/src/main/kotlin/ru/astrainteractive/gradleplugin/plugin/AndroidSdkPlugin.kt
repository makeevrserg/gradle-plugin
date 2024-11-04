package ru.astrainteractive.gradleplugin.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.extension.AndroidModelPropertyValueExt.requireAndroidSdkInfo
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo

class AndroidSdkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val jinfo = target.requireJinfo
        val androidSdkInfo = target.requireAndroidSdkInfo
        target.configure<BaseExtension> {
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
}
