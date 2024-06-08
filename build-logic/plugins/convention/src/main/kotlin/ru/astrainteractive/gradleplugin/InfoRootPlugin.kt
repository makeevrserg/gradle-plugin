package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.hierarchyGroup
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

class InfoRootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectInfo = target.requireProjectInfo
        if (target.rootProject.group.toString().isEmpty()) {
            target.rootProject.group = projectInfo.group
        }
        if (target.group.toString().isEmpty()) {
            target.group = target.hierarchyGroup
        }
        if (target.version.toString().isEmpty()) {
            target.version = projectInfo.versionString
        }
        if (target.description.isNullOrBlank()) {
            target.description = projectInfo.description
        }
    }
}
