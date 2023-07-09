package ru.astrainteractive.gradleplugin.processors

import org.gradle.api.Project
import org.gradle.kotlin.dsl.expand
import org.gradle.kotlin.dsl.named
import ru.astrainteractive.gradleplugin.models.Developer
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

internal class VelocityResourceProcessorImpl(private val project: Project) : SpigotResourceProcessor {

    override fun process() {
        val projectInfo = project.projectInfo
        val processorInfo = VelocityResourceProcessor.VelocityProcessorInfo(
            main = "${projectInfo.group}.${projectInfo.name}",
            name = projectInfo.name,
            version = projectInfo.versionString,
            url = projectInfo.url,
            authors = projectInfo.developersList,
            id = projectInfo.name.toLowerCase()
        )
        project.tasks.named<org.gradle.language.jvm.tasks.ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            inputs.property("version", processorInfo.version)
            filesMatching("velocity-plugin.json") {
                expand(
                    "id" to processorInfo.id,
                    "name" to processorInfo.name,
                    "version" to processorInfo.version,
                    "url" to processorInfo.url,
                    "authors" to processorInfo.authors.map(Developer::id).joinToString("\",\""),
                    "main" to processorInfo.main
                )
            }
        }
    }
}
