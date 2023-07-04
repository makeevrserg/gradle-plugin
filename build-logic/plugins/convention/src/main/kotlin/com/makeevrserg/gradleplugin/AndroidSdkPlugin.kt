package com.makeevrserg.gradleplugin

import com.android.build.gradle.BaseExtension
import libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidSdkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<BaseExtension> {
            compileSdkVersion(target.libs.versions.project.sdk.compile.get().toInt())

            defaultConfig {
                minSdk = target.libs.versions.project.sdk.min.get().toInt()
                targetSdk = target.libs.versions.project.sdk.target.get().toInt()
            }
            compileOptions {
                sourceCompatibility = ConventionProject.TARGET_JAVA_VERSION
                targetCompatibility = ConventionProject.SOURCE_JAVA_VERSION
            }
        }
    }
}
