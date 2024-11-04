package ru.astrainteractive.gradleplugin.processor.platform

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.processor.core.ResourceProcessor
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

internal class FabricResourceProcessor(private val project: Project) : ResourceProcessor<FabricResourceProcessor.Info> {

    data class Info(
        val version: String,
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
        val projectInfo = project.requireProjectInfo
        return Info(
            version = projectInfo.versionString,
        )
    }

    override fun getDefaultProperties(): Map<String, String> {
        val processorInfo = getProcessorInfo()
        return mapOf(
            "version" to processorInfo.version
        )
    }

    override fun process(
        customProperties: Map<String, String>,
        configuration: ProcessResources.() -> Unit
    ) {
        project.tasks.named<ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.WARN
            filesMatching("fabric.mod.json") {
                expand(getDefaultProperties().plus(customProperties))
            }
            configuration.invoke(this)
        }
    }
}
