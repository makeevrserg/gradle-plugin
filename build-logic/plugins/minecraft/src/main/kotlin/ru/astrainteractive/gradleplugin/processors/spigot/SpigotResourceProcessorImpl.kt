package ru.astrainteractive.gradleplugin.processors.spigot

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.expand
import org.gradle.kotlin.dsl.named
import ru.astrainteractive.gradleplugin.models.Developer
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

internal class SpigotResourceProcessorImpl(private val project: Project) : SpigotResourceProcessor {

    override fun process() {
        val projectInfo = project.projectInfo
        val processorInfo = SpigotResourceProcessor.SpigotProcessorInfo(
            main = "${projectInfo.group}.${projectInfo.name}",
            name = projectInfo.name,
            prefix = projectInfo.name,
            version = projectInfo.versionString,
            description = projectInfo.description,
            url = projectInfo.url,
            author = projectInfo.developersList.first(),
            authors = projectInfo.developersList,
            libraries = emptyList() // project.gradleProperty("minecraft.libraries").string.split(";"), // todo
        )
        project.tasks.named<org.gradle.language.jvm.tasks.ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            filesMatching("plugin.yml") {
                expand(
                    "main" to processorInfo.main,
                    "name" to processorInfo.name,
                    "prefix" to processorInfo.prefix,
                    "version" to processorInfo.version,
                    "description" to processorInfo.description,
                    "url" to processorInfo.url,
                    "author" to processorInfo.author.id,
                    "authors" to processorInfo.authors.map(Developer::id).joinToString("\",\""),
                    "libraries" to processorInfo.libraries.joinToString("\",\""),
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}
