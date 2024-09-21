package ru.astrainteractive.gradleplugin.processors

import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.processors.core.ResourceProcessor
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

class FabricResourceProcessor(private val project: Project) : ResourceProcessor<FabricResourceProcessor.Info> {

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

    override fun process(configuration: ProcessResources.() -> Unit) {
        val processorInfo = getProcessorInfo()
        project.tasks.named<ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            inputs.property("version", processorInfo.version)
            filesMatching("fabric.mod.json") {
                expand(getDefaultProperties())
            }
            configuration.invoke(this)
        }
    }
}
