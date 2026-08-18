package ru.astrainteractive.gradleplugin.plugin.minecraft

import org.gradle.api.Project
import org.gradle.api.artifacts.FileCollectionDependency
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.ForgePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NeoForgePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.platform.ForgePlatformPlugin
import ru.astrainteractive.gradleplugin.plugin.minecraft.platform.NeoForgePlatformPlugin
import java.io.File

class LocalPlatformJarTest {
    private fun moduleOf(rootDirectory: File): Project {
        val rootProject = ProjectBuilder.builder()
            .withProjectDir(rootDirectory)
            .build()
        val module = ProjectBuilder.builder()
            .withName("moduleA")
            .withParent(rootProject)
            .build()
        module.pluginManager.apply("java-library")
        return module
    }

    private fun Project.singleCompileOnlyFile(): File {
        val dependency = configurations.getByName("compileOnly")
            .dependencies
            .single() as FileCollectionDependency
        return dependency.files.singleFile.canonicalFile
    }

    @Test
    fun `forge takes the local jar from the root directory`(@TempDir rootDirectory: File) {
        val platform = ForgePlatform().apply {
            version = "1.20.1-47.2.0"
            useLocal = true
        }
        val module = moduleOf(rootDirectory)

        ForgePlatformPlugin(platform).apply(module)

        assertEquals(
            rootDirectory.resolve(".gradle/mavenizer/repo/net/minecraftforge/forge")
                .resolve(platform.version)
                .resolve("forge-${platform.version}.jar")
                .canonicalFile,
            module.singleCompileOnlyFile()
        )
    }

    @Test
    fun `neoforge takes the local jar from the root directory`(@TempDir rootDirectory: File) {
        val platform = NeoForgePlatform().apply {
            version = "21.1.77"
            useLocal = true
        }
        val module = moduleOf(rootDirectory)

        NeoForgePlatformPlugin(platform).apply(module)

        assertEquals(
            rootDirectory.resolve(".gradle/repositories/ng_dummy_ng/net/neoforged/neoforge")
                .resolve(platform.version)
                .resolve("neoforge-${platform.version}.jar")
                .canonicalFile,
            module.singleCompileOnlyFile()
        )
    }
}
