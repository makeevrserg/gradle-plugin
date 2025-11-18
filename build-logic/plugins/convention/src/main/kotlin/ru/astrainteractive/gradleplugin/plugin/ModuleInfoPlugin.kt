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
        if (target.project != target.rootProject) {
            target.logger.warn("You've applied ModuleInfoPlugin to non-root project!")
        }

        target.group = projectInfo.group
        target.version = projectInfo.versionString
        target.description = projectInfo.description
    }
}
