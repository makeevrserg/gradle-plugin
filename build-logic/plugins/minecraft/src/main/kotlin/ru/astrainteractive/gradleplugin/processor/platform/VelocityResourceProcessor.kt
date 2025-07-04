package ru.astrainteractive.gradleplugin.processor.platform

import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.model.Developer
import ru.astrainteractive.gradleplugin.model.ProjectInfo
import ru.astrainteractive.gradleplugin.processor.core.ResourceProcessor
import java.util.Locale

internal class VelocityResourceProcessor(
    private val projectInfo: ProjectInfo
) : ResourceProcessor<VelocityResourceProcessor.Info> {

    data class Info(
        val main: String,
        val name: String,
        val version: String,
        val url: String,
        val authors: List<Developer>,
        val id: String,
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
        return Info(
            main = "${projectInfo.group}.${projectInfo.name}",
            name = projectInfo.name,
            version = projectInfo.versionString,
            url = projectInfo.url,
            authors = projectInfo.developersList,
            id = projectInfo.name.lowercase(Locale.getDefault())
        )
    }

    override fun getDefaultProperties(): Map<String, String> {
        val processorInfo = getProcessorInfo()
        return mapOf(
            "id" to processorInfo.id,
            "name" to processorInfo.name,
            "version" to processorInfo.version,
            "url" to processorInfo.url,
            "authors" to processorInfo.authors.map(Developer::id).joinToString("\",\""),
            "main" to processorInfo.main
        )
    }

    override fun process(
        task: TaskProvider<ProcessResources>,
        customProperties: Map<String, String>,
    ) {
        task.configure {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.WARN
            filesMatching("velocity-plugin.json") {
                expand(getDefaultProperties().plus(customProperties))
            }
        }
    }
}
