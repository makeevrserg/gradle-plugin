package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.impl.VariantOutputImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

class ApkNamePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectInfo = target.requireProjectInfo
        target.configure<ApplicationAndroidComponentsExtension> {

            onVariants { variant ->
                variant.outputs.onEach { output ->
                    if (output is VariantOutputImpl) {
                        val name = projectInfo.name
                        val version = projectInfo.versionString
                        output.outputFileName.set("${name}_${version}_${variant.name}.apk")
                    }
                }
            }
        }
    }
}
