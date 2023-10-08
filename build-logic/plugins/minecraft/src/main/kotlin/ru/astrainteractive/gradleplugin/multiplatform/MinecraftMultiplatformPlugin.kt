package ru.astrainteractive.gradleplugin.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class MinecraftMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("minecraftMultiplatform", MinecraftMultiplatformScope::class, target)
    }
}
