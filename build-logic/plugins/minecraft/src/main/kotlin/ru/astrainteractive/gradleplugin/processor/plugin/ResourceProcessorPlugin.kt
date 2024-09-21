package ru.astrainteractive.gradleplugin.processor.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ResourceProcessorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            name = "minecraftProcessResource",
            type = ResourceProcessorScope::class,
            target
        )
    }
}
