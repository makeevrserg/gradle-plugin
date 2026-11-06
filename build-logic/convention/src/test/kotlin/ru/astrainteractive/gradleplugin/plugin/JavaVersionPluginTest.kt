package ru.astrainteractive.gradleplugin.plugin

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import ru.astrainteractive.gradleplugin.fixture.IsolatedProjectsBuild
import java.io.File

class JavaVersionPluginTest {
    private val moduleScript = """
        plugins {
            `java-library`
            id("ru.astrainteractive.gradleplugin.java.version")
        }

        val toolchainVersion = java.toolchain.languageVersion.map { version -> version.toString() }.orElse("none")
        tasks.register("printToolchain") {
            val version = toolchainVersion
            doLast { println("toolchain=" + version.get()) }
        }
    """.trimIndent()

    @Test
    fun `no toolchain is requested by default`(@TempDir rootDirectory: File) {
        val result = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "moduleA", buildScript = moduleScript)
            .run(":moduleA:printToolchain")

        assertTrue(result.output.contains("toolchain=none"), result.output)
    }

    @Test
    fun `toolchain follows the java target when use_toolchain is true`(@TempDir rootDirectory: File) {
        val result = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "moduleA", buildScript = moduleScript)
            .run(":moduleA:printToolchain", "-Pklibs.java.use_toolchain=true")

        assertTrue(result.output.contains("toolchain=17"), result.output)
    }
}
