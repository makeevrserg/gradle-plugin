package ru.astrainteractive.gradleplugin.processors.forge

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.expand
import org.gradle.kotlin.dsl.named
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

class ForgeResourceProcessorImpl(private val project: Project) : ForgeResourceProcessor {
    override fun process() {
        val projectInfo = project.requireProjectInfo
        val processorInfo = ForgeResourceProcessor.Info(
            displayName = projectInfo.name,
            version = projectInfo.versionString,
            description = projectInfo.description,
        )
        project.tasks.named<org.gradle.language.jvm.tasks.ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            filesMatching("mods.toml") {
                expand(
                    "{displayName}" to processorInfo.displayName,
                    "{version}" to processorInfo.version,
                    "{description}" to processorInfo.description
                )
            }
        }
    }
}
