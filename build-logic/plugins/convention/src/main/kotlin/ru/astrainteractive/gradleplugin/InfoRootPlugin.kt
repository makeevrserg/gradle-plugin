package ru.astrainteractive.gradleplugin

import ru.astrainteractive.gradleplugin.util.GradleProperty.Companion.gradleProperty
import org.gradle.api.Plugin
import org.gradle.api.Project

class InfoRootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.rootProject.allprojects {
            group = target.gradleProperty("project.group").string
            version = target.gradleProperty("project.version.string").string
            description = target.gradleProperty("project.description").string
        }
    }
}
