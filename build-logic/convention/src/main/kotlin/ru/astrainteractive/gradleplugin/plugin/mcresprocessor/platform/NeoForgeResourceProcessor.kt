package ru.astrainteractive.gradleplugin.plugin.mcresprocessor.platform

import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.plugin.mcresprocessor.api.ResourceProcessor
import ru.astrainteractive.gradleplugin.property.model.Developer
import ru.astrainteractive.gradleplugin.property.model.ProjectInfo

internal class NeoForgeResourceProcessor(
    private val projectInfo: ProjectInfo
) : ResourceProcessor<NeoForgeResourceProcessor.Info> {

    data class Info(
        val modId: String,
        val displayName: String,
        val version: String,
        val description: String,
        val developers: List<Developer>
    ) : ResourceProcessor.ProcessorInfo

    override fun getProcessorInfo(): Info {
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
            "neo_version" to "neo_version",
            "mod_id" to processorInfo.modId,
            "mod_name" to processorInfo.displayName,
            "mod_license" to "mod_license",
            "mod_version" to processorInfo.version,
            "mod_authors" to processorInfo.developers
                .map(Developer::id)
                .joinToString(","),
            "mod_description" to processorInfo.description
        )
    }

    override fun process(
        task: TaskProvider<ProcessResources>,
        customProperties: Map<String, String>,
    ) {
        task.configure {
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.WARN
            val sourceSets = project.extensions.getByName("sourceSets") as SourceSetContainer
            val resDirs = sourceSets
                .map(SourceSet::getResources)
                .map(SourceDirectorySet::getSrcDirs)
            from(resDirs) {
                include("META-INF/mods.toml")
                expand(getDefaultProperties().plus(customProperties))
            }
        }
    }
}
