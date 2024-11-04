package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

/**
 * Sets group, version and description of module from gradle.properties
 */
class ModuleInfoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectInfo = target.requireProjectInfo

        target.group = projectInfo.group
        target.version = projectInfo.versionString
        target.description = projectInfo.description
    }
}
