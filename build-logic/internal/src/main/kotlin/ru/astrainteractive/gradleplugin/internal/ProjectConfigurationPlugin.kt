package ru.astrainteractive.gradleplugin.internal

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class ProjectConfigurationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val configuration = ProjectConfiguration(
            name = target.requireProjectProperty("klibs.project.name"),
            description = target.requireProjectProperty("klibs.project.description"),
            group = target.requireProjectProperty("klibs.project.group"),
            url = target.requireProjectProperty("klibs.project.url"),
            versionString = target.requireProjectProperty("klibs.project.version.string")
        )
        target.extensions.add(ProjectConfiguration::class.java, EXTENSION_NAME, configuration)
    }

    private fun Project.requireProjectProperty(key: String): String {
        return providers.gradleProperty(key).orNull
            ?: throw GradleException("Could not find property $key in gradle.properties")
    }

    companion object {
        const val EXTENSION_NAME = "klibs"
    }
}
