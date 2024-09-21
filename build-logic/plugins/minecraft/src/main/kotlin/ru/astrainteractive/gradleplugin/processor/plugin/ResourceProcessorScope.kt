package ru.astrainteractive.gradleplugin.processor.plugin

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.processor.platform.FabricResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.ForgeResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.SpigotResourceProcessor
import ru.astrainteractive.gradleplugin.processor.platform.VelocityResourceProcessor

open class ResourceProcessorScope(project: Project) {
    val spigotResourceProcessor: SpigotResourceProcessor = SpigotResourceProcessor(project)
    val velocityResourceProcessor: VelocityResourceProcessor = VelocityResourceProcessor(project)
    val forgeResourceProcessor: ForgeResourceProcessor = ForgeResourceProcessor(project)
    val fabricResourceProcessor: FabricResourceProcessor = FabricResourceProcessor(project)
}