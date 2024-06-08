package ru.astrainteractive.gradleplugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.hierarchyGroup

class AndroidNamespacePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            val baseExtension = extensions.findByType<BaseExtension>()
            if (baseExtension?.namespace.isNullOrBlank()) {
                baseExtension?.namespace = target.hierarchyGroup
            }
        }
    }
}
