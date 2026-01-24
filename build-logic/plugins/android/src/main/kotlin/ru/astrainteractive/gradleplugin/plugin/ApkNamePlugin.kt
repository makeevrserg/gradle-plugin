package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.impl.VariantOutputImpl
import com.android.build.gradle.AbstractAppExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo
import java.io.File

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
