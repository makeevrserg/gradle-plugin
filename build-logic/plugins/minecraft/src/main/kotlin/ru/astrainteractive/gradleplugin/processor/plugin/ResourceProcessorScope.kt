package ru.astrainteractive.gradleplugin.processor.plugin

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.model.ProjectInfo
import ru.astrainteractive.gradleplugin.processor.platform.BukkitResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.FabricResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.ForgeResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.VelocityResourceProcessor

open class ResourceProcessorScope(private val projectInfo: ProjectInfo) {
    val Project.task: TaskProvider<ProcessResources>
        get() = tasks.named<ProcessResources>("processResources")

    fun Project.bukkit(
        customProperties: Map<String, String> = emptyMap(),
    ) = BukkitResourceProcessor(projectInfo).process(task, customProperties)

    fun Project.velocity(
        customProperties: Map<String, String> = emptyMap(),
    ) = VelocityResourceProcessor(projectInfo).process(task, customProperties)

    fun Project.fabric(
        customProperties: Map<String, String> = emptyMap(),
    ) = FabricResourceProcessor(projectInfo).process(task, customProperties)

    fun Project.forge(
        customProperties: Map<String, String> = emptyMap(),
    ) = ForgeResourceProcessor(projectInfo).process(task, customProperties)
}
