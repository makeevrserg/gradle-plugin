package ru.astrainteractive.gradleplugin.processor.platform

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.processor.core.ResourceProcessor
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

class ForgeResourceProcessor(private val project: Project) : ResourceProcessor<ForgeResourceProcessor.Info> {

    data class Info(
        val displayName: String,
        val version: String,
        val description: String,
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
        val projectInfo = project.requireProjectInfo
        return Info(
            displayName = projectInfo.name,
            version = projectInfo.versionString,
            description = projectInfo.description,
        )
    }

    override fun getDefaultProperties(): Map<String, String> {
        val processorInfo = getProcessorInfo()
        return mapOf(
            "{displayName}" to processorInfo.displayName,
            "{version}" to processorInfo.version,
            "{description}" to processorInfo.description
        )
    }

    override fun process(configuration: ProcessResources.() -> Unit) {
        project.tasks.named<ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            filesMatching("mods.toml") {
                expand(getDefaultProperties())
            }
        }
    }
}
