package ru.astrainteractive.gradleplugin.processor.platform

import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.model.ProjectInfo
import ru.astrainteractive.gradleplugin.processor.core.ResourceProcessor

internal class FabricResourceProcessor(
    private val projectInfo: ProjectInfo
) : ResourceProcessor<FabricResourceProcessor.Info> {

    data class Info(
        val version: String,
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
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
        task: TaskProvider<ProcessResources>,
        customProperties: Map<String, String>,
    ) {
        task.configure {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.WARN
            filesMatching("fabric.mod.json") {
                expand(getDefaultProperties().plus(customProperties))
            }
        }
    }
}
