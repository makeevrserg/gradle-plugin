package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class EmptyMinecraftPlugin : Plugin<Project> {
    override fun apply(target: Project) = Unit
}
