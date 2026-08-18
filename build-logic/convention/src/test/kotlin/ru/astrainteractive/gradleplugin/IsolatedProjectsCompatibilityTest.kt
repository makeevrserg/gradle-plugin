package ru.astrainteractive.gradleplugin

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.astrainteractive.gradleplugin.fixture.IsolatedProjectsBuild
import java.io.File

class IsolatedProjectsCompatibilityTest {
    @ParameterizedTest(name = "{0} configures every module without coupling projects")
    @ValueSource(
        strings = [
            "ru.astrainteractive.gradleplugin.detekt",
            "ru.astrainteractive.gradleplugin.dokka",
            "ru.astrainteractive.gradleplugin.java.version",
            "ru.astrainteractive.gradleplugin.java.utf8",
            "ru.astrainteractive.gradleplugin.publication",
            "ru.astrainteractive.gradleplugin.minecraft.resource.processor",
        ]
    )
    fun `plugin keeps projects isolated`(pluginId: String, @TempDir rootDirectory: File) {
        val moduleScript = """
            plugins {
                `java-library`
                id("$pluginId")
            }
        """.trimIndent()
        val result = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "moduleA", buildScript = moduleScript)
            .withModule(name = "moduleB", buildScript = moduleScript)
            .run(":moduleA:tasks", ":moduleB:tasks")

        assertFalse(
            result.output.contains("cannot access"),
            "$pluginId reached into another project:\n${result.output}"
        )
    }

    @ParameterizedTest(name = "{0} configures the root project without coupling projects")
    @ValueSource(strings = ["ru.astrainteractive.gradleplugin.rootinfo"])
    fun `root plugin keeps projects isolated`(pluginId: String, @TempDir rootDirectory: File) {
        val result = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript(
                """
                plugins {
                    id("$pluginId")
                }
                """.trimIndent()
            )
            .withModule(name = "moduleA", buildScript = "plugins { `java-library` }")
            .run("tasks", ":moduleA:tasks")

        assertFalse(
            result.output.contains("cannot access"),
            "$pluginId reached into another project:\n${result.output}"
        )
    }
}
