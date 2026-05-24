package ru.astrainteractive.gradleplugin.plugin.minecraft

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.ForgePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NativePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NeoForgePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.platform.ForgePlatformPlugin
import ru.astrainteractive.gradleplugin.plugin.minecraft.platform.NativePlatformPlugin
import ru.astrainteractive.gradleplugin.plugin.minecraft.platform.NeoForgePlatformPlugin

class MinecraftPlatformPlugin : Plugin<Project> {

    private fun applyPlatform(target: Project) {
        val platform = target.extensions
            .getByType<MinecraftPlatformExtension>()
            .platform

        if (platform == null) {
            target.logger.error("[$EXTENSION_NAME] 'platform' must be set inside the '$EXTENSION_NAME { }' block.")
            return
        }

        when (platform) {
            is NativePlatform -> NativePlatformPlugin(platform).apply(target)
            is ForgePlatform -> ForgePlatformPlugin(platform).apply(target)
            is NeoForgePlatform -> NeoForgePlatformPlugin(platform).apply(target)
        }
    }

    override fun apply(target: Project) {
        target.extensions.create<MinecraftPlatformExtension>(EXTENSION_NAME)
        applyPlatform(target)
    }

    private companion object {
        const val EXTENSION_NAME = "minecraftPlatform"
    }
}
