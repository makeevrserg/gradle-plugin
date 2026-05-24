package ru.astrainteractive.gradleplugin.plugin.minecraft.platform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NeoForgePlatform

internal class NeoForgePlatformPlugin(private val platform: NeoForgePlatform) : Plugin<Project> {
    private fun applyLocal(target: Project) {
        val jar = target.rootProject.file(".gradle")
            .resolve("repositories")
            .resolve("ng_dummy_ng")
            .resolve("net")
            .resolve("neoforged")
            .resolve("neoforge")
            .resolve(platform.version)
            .resolve("neoforge-${platform.version}.jar")
        target.dependencies { add("compileOnly", target.files(jar)) }
    }

    private fun applyPlugin(target: Project) {
        target.pluginManager.apply("net.neoforged.gradle.userdev")
        target.dependencies { add("compileOnly", "net.neoforged:neoforge:${platform.version}") }
    }

    override fun apply(target: Project) {
        require(platform.version.isNotBlank()) { "neoforge { version } must not be blank" }
        if (platform.useLocal) applyLocal(target) else applyPlugin(target)
    }
}
