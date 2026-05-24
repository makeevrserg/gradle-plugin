package ru.astrainteractive.gradleplugin.plugin.minecraft.platform

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NativePlatform

internal class NativePlatformPlugin(private val platform: NativePlatform) : Plugin<Project> {

    override fun apply(target: Project) {
        require(platform.version.isNotBlank()) { "native { version } must not be blank" }

        target.pluginManager.apply("fabric-loom")

        val loom = target.extensions.getByType<LoomGradleExtensionAPI>()

        target.dependencies {
            add("minecraft", "com.mojang:minecraft:${platform.version}")
            add("mappings", loom.officialMojangMappings())
        }
    }
}
