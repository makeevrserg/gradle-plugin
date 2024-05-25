package ru.astrainteractive.gradleplugin

import com.android.build.gradle.AbstractAppExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

/**
 * This plugin will name output android apk file with custom name like below:
 *
 * ProjectName_0.0.1_debug.apk
 */
class ApkNamePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectInfo = target.requireProjectInfo
        target.configure<AbstractAppExtension> {
            buildTypes {
                applicationVariants.onEach { variant ->
                    variant.outputs.onEach { output ->
                        if (output is BaseVariantOutputImpl) {
                            val name = projectInfo.name
                            val version = projectInfo.versionString
                            output.outputFileName = "${name}_${version}_${variant.name}.apk"
                        }
                    }
                }
            }
        }
    }
}
