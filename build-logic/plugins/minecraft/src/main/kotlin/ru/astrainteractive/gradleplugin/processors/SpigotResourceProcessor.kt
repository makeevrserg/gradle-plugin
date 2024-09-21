package ru.astrainteractive.gradleplugin.processors

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.model.Developer
import ru.astrainteractive.gradleplugin.processors.core.ResourceProcessor
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

class SpigotResourceProcessor(private val project: Project) : ResourceProcessor<SpigotResourceProcessor.Info> {

    data class Info(
        val main: String,
        val name: String,
        val prefix: String,
        val version: String,
        val description: String,
        val url: String,
        val author: Developer,
        val authors: List<Developer>,
        val libraries: List<String>
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
        val projectInfo = project.requireProjectInfo
        return Info(
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
    }

    override fun getDefaultProperties(): Map<String, String> {
        val processorInfo = getProcessorInfo()
        return mapOf(
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

    override fun process(configuration: ProcessResources.() -> Unit) {
        project.tasks.named<ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            filesMatching("plugin.yml") {
                expand(getDefaultProperties())
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}
