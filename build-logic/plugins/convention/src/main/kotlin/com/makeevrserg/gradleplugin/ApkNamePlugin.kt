package com.makeevrserg.gradleplugin

import com.android.build.gradle.AbstractAppExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.makeevrserg.gradleplugin.util.GradleProperty.Companion.gradleProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * This plugin will name output android apk file with custom name like below:
 *
 * ProjectName_0.0.1_debug.apk
 */
class ApkNamePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<AbstractAppExtension> {
            buildTypes {
                applicationVariants.onEach { variant ->
                    variant.outputs.onEach { output ->
                        if (output is BaseVariantOutputImpl) {
                            val name = target.gradleProperty("project.name").string
                            val version = target.gradleProperty("project.version.string").string
                            output.outputFileName = "${name}_${version}_${variant.name}.apk"
                        }
                    }
                }
            }
        }
    }
}
