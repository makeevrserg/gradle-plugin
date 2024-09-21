package ru.astrainteractive.gradleplugin.processors.plugin

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.processors.FabricResourceProcessor
import ru.astrainteractive.gradleplugin.processors.ForgeResourceProcessor
import ru.astrainteractive.gradleplugin.processors.SpigotResourceProcessor
import ru.astrainteractive.gradleplugin.processors.VelocityResourceProcessor

open class ResourceProcessorScope(project: Project) {
    val spigotResourceProcessor: SpigotResourceProcessor = SpigotResourceProcessor(project)
    val velocityResourceProcessor: VelocityResourceProcessor = VelocityResourceProcessor(project)
    val forgeResourceProcessor: ForgeResourceProcessor = ForgeResourceProcessor(project)
    val fabricResourceProcessor: FabricResourceProcessor = FabricResourceProcessor(project)
}