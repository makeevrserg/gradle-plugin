package ru.astrainteractive.gradleplugin.plugin.detekt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import ru.astrainteractive.gradleplugin.fixture.IsolatedProjectsBuild
import java.io.File

class DetektSharedConfigTest {
    private fun buildWithTwoModules(rootDirectory: File): IsolatedProjectsBuild {
        val moduleScript = """
            import dev.detekt.gradle.Detekt

            plugins {
                `java-library`
                id("ru.astrainteractive.gradleplugin.detekt")
            }

            val detektConfig = tasks.named<Detekt>("detekt").get().config
            val configReport = layout.buildDirectory.file("$CONFIG_REPORT_NAME")
            tasks.register("reportDetektConfig") {
                val configFiles = detektConfig
                val reportFile = configReport
                inputs.files(configFiles)
                outputs.file(reportFile)
                doLast {
                    val paths = configFiles.files.joinToString(separator = "\n") { file -> file.absolutePath }
                    reportFile.get().asFile.writeText(paths)
                }
            }
        """.trimIndent()
        return IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "moduleA", buildScript = moduleScript)
            .withModule(name = "moduleB", buildScript = moduleScript)
    }

    private fun IsolatedProjectsBuild.reportedConfigFile(moduleName: String): File {
        val reported = file("$moduleName/build/$CONFIG_REPORT_NAME").readLines()
        assertEquals(1, reported.size, "expected exactly one detekt configuration file in $moduleName")
        return File(reported.first()).canonicalFile
    }

    @Test
    fun `every module points at one configuration file in the root build directory`(@TempDir rootDirectory: File) {
        val build = buildWithTwoModules(rootDirectory)
        build.run(":moduleA:reportDetektConfig", ":moduleB:reportDetektConfig")

        val configFile = build.reportedConfigFile("moduleA")
        assertEquals(configFile, build.reportedConfigFile("moduleB"))
        assertEquals(build.file("build/detekt/detekt.yml").canonicalFile, configFile)
        assertTrue(
            configFile.readText().contains("ktlint:"),
            "the bundled detekt configuration was not written into $configFile"
        )
    }

    @Test
    fun `one configuration file exists in the whole build`(@TempDir rootDirectory: File) {
        val build = buildWithTwoModules(rootDirectory)
        build.run(":moduleA:reportDetektConfig", ":moduleB:reportDetektConfig")

        val writtenConfigurations = rootDirectory.walkTopDown()
            .filter { file -> file.name == "detekt.yml" }
            .map(File::getCanonicalFile)
            .toList()
        assertEquals(
            listOf(build.file("build/detekt/detekt.yml").canonicalFile),
            writtenConfigurations
        )
    }

    @Test
    fun `configuration is written again when the build directory is gone`(@TempDir rootDirectory: File) {
        val build = buildWithTwoModules(rootDirectory)
        build.run(":moduleA:reportDetektConfig")
        val configFile = build.file("build/detekt/detekt.yml")
        assertTrue(configFile.delete(), "could not delete $configFile")

        val result = build.run(":moduleA:reportDetektConfig")

        assertTrue(
            result.output.contains("Reusing configuration cache"),
            "the second run did not hit the configuration cache:\n${result.output}"
        )
        assertTrue(configFile.isFile, "$configFile was not written again on a configuration cache hit")
    }

    private companion object {
        const val CONFIG_REPORT_NAME = "detekt-config-path.txt"
    }
}
