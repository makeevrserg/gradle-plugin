package com.makeevrserg.gradleplugin

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import libs
import org.gradle.api.Action
import org.gradle.api.Project

/**
 * This plugin will name output android apk file with custom name like below:
 *
 * ProjectName_0.0.1_debug.apk
 */
class ApplicationVariantAction(private val project: Project) : Action<ApplicationVariant> {
    override fun execute(variant: ApplicationVariant) {
        val fileName = createFileName(variant)
        variant.outputs.all(VariantOutputAction(fileName))
    }

    private fun createFileName(variant: ApplicationVariant): String {
        return project.libs.versions.project.name.get() +
            "_${project.libs.versions.project.version.string.get()}" +
            "_${variant.name}" +
            ".apk"
    }

    class VariantOutputAction(
        private val fileName: String
    ) : Action<BaseVariantOutput> {
        override fun execute(output: BaseVariantOutput) {
            if (output is BaseVariantOutputImpl) {
                output.outputFileName = fileName
            }
        }
    }
}
