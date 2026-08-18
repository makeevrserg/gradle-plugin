package ru.astrainteractive.gradleplugin.plugin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import ru.astrainteractive.gradleplugin.fixture.IsolatedProjectsBuild
import java.io.File

class KobwebResourcesPluginTest {
    @Test
    fun `resources of another project are copied without coupling projects`(@TempDir rootDirectory: File) {
        val consumerScript = """
            plugins {
                `java-library`
                id("ru.astrainteractive.gradleplugin.js.kobweb.resources")
            }

            jsResources {
                projectsPaths = listOf(":moduleB")
            }
        """.trimIndent()
        val build = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "moduleA", buildScript = consumerScript)
            .withModule(name = "moduleB", buildScript = "plugins { `java-library` }")
            .withSourceFile(
                path = "moduleB/src/jsMain/resources/public/index.html",
                content = "<html></html>"
            )

        build.run(":moduleA:copyJsResources")

        val copied = build.file("moduleA/build/processedResources/js/main/public/index.html")
        assertEquals("<html></html>", copied.readText())
    }
}
