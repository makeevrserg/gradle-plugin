package ru.astrainteractive.gradleplugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.util.ProjectProperties.androidSdkInfo
import ru.astrainteractive.gradleplugin.util.ProjectProperties.jinfo

class AndroidSdkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val jinfo = target.jinfo
        val androidSdkInfo = target.androidSdkInfo
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
