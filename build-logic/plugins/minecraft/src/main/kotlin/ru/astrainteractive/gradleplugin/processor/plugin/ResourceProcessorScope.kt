package ru.astrainteractive.gradleplugin.processor.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.processor.platform.BukkitResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.FabricResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.ForgeResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.VelocityResourceProcessor

open class ResourceProcessorScope(private val project: Project) {
    val task = project.tasks.named<ProcessResources>("processResources")

    fun bukkit(
        customProperties: Map<String, String> = emptyMap(),
        configuration: ProcessResources.() -> Unit = {}
    ) = BukkitResourceProcessor(project).process(customProperties, configuration)

    fun velocity(
        customProperties: Map<String, String> = emptyMap(),
        configuration: ProcessResources.() -> Unit = {}
    ) = VelocityResourceProcessor(project).process(customProperties, configuration)

    fun fabric(
        customProperties: Map<String, String> = emptyMap(),
        configuration: ProcessResources.() -> Unit = {}
    ) = FabricResourceProcessor(project).process(customProperties, configuration)

    fun forge(
        customProperties: Map<String, String> = emptyMap(),
        configuration: ProcessResources.() -> Unit = {}
    ) = ForgeResourceProcessor(project).process(customProperties, configuration)
}
