package ru.astrainteractive.gradleplugin.processor.platform

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.model.Developer
import ru.astrainteractive.gradleplugin.processor.core.ResourceProcessor
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

internal class ForgeResourceProcessor(private val project: Project) : ResourceProcessor<ForgeResourceProcessor.Info> {

    data class Info(
        val modId: String,
        val displayName: String,
        val version: String,
        val description: String,
        val developers: List<Developer>
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
        val projectInfo = project.requireProjectInfo
        return Info(
            modId = projectInfo.name.lowercase(),
            displayName = projectInfo.name,
            version = projectInfo.versionString,
            description = projectInfo.description,
            developers = projectInfo.developersList
        )
    }

    override fun getDefaultProperties(): Map<String, String> {
        val processorInfo = getProcessorInfo()
        return mapOf(
            "modId" to processorInfo.modId,
            "version" to processorInfo.version,
            "description" to processorInfo.description,
            "displayName" to processorInfo.displayName,
            "authors" to processorInfo.developers.map(Developer::id).joinToString(",")
        )
    }

    override fun process(
        customProperties: Map<String, String>,
        configuration: ProcessResources.() -> Unit
    ) {
        project.tasks.named<ProcessResources>("processResources") {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.WARN
            val sourceSets = project.extensions.getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer
            val resDirs = sourceSets
                .map(SourceSet::getResources)
                .map(SourceDirectorySet::getSrcDirs)
            from(resDirs) {
                include("META-INF/mods.toml")
                expand(getDefaultProperties().plus(customProperties))
            }
            configuration.invoke(this)
        }
    }
}
