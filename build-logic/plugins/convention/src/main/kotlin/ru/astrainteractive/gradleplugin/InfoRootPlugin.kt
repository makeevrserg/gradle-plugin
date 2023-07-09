package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

class InfoRootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectInfo = target.projectInfo
        target.rootProject.allprojects {
            group = projectInfo.group
            version = projectInfo.versionString
            description = projectInfo.description
        }
    }
}
