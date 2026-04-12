package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import ru.astrainteractive.gradleplugin.plugin.mcresprocessor.ResourceProcessorScope
import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo

class ResourceProcessorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            name = "minecraftProcessResource",
            type = ResourceProcessorScope::class,
            target.requireProjectInfo
        )
    }
}
