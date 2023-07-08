package ru.astrainteractive.gradleplugin

import com.android.build.gradle.BaseExtension
import ru.astrainteractive.gradleplugin.util.GradleProperty.Companion.gradleProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidSdkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<BaseExtension> {
            compileSdkVersion(target.gradleProperty("android.sdk.compile").integer)

            defaultConfig {
                minSdk = target.gradleProperty("android.sdk.min").integer
                targetSdk = target.gradleProperty("android.sdk.target").integer
            }
            compileOptions {
                sourceCompatibility = target.gradleProperty("java.source").javaVersion
                targetCompatibility = target.gradleProperty("java.target").javaVersion
            }
        }
    }
}
