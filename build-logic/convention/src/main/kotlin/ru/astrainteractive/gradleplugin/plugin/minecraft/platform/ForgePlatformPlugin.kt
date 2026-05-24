package ru.astrainteractive.gradleplugin.plugin.minecraft.platform

import net.minecraftforge.gradle.MinecraftExtensionForProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.ForgePlatform

internal class ForgePlatformPlugin(private val platform: ForgePlatform) : Plugin<Project> {

    private fun applyLocal(target: Project) {
        val jar = target.rootProject.file(".gradle")
            .resolve("mavenizer")
            .resolve("repo")
            .resolve("net")
            .resolve("minecraftforge")
            .resolve("forge")
            .resolve(platform.version)
            .resolve("forge-${platform.version}.jar")
        target.dependencies { add("compileOnly", target.files(jar)) }
    }

    private fun applyPlugin(target: Project) {
        target.pluginManager.apply("net.minecraftforge.gradle")

        val minecraftExt = target.extensions.getByType<MinecraftExtensionForProject>()

        minecraftExt.mavenizer(target.repositories)

        target.repositories {
            maven("https://maven.minecraftforge.net/")
            maven("https://libraries.minecraft.net/")
            mavenCentral()
            mavenLocal()
        }

        target.dependencies {
            add(
                "compileOnly",
                minecraftExt.dependency("net.minecraftforge:forge:${platform.version}")
            )
        }
    }

    override fun apply(target: Project) {
        require(platform.version.isNotBlank()) { "forge { version } must not be blank" }
        if (platform.useLocal) applyLocal(target) else applyPlugin(target)
    }
}
