package ru.astrainteractive.gradleplugin.processors.fabric

import org.gradle.api.Project
import org.gradle.kotlin.dsl.expand
import org.gradle.kotlin.dsl.named
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

class FabricResourceProcessorImpl(private val project: Project) : FabricResourceProcessor {

    override fun process() {
        val projectInfo = project.projectInfo
        val processorInfo = FabricResourceProcessor.Info(
            version = projectInfo.versionString,
        )
        project.tasks.named<org.gradle.language.jvm.tasks.ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            inputs.property("version", processorInfo.version)
            filesMatching("fabric.mod.json") {
                expand(
                    "version" to processorInfo.version
                )
            }
        }
    }
}
