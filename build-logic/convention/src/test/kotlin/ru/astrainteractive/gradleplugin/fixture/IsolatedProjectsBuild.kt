package ru.astrainteractive.gradleplugin.fixture

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

internal class IsolatedProjectsBuild(private val rootDirectory: File) {
    private val moduleNames = mutableListOf<String>()

    private fun writeFile(path: String, content: String) {
        val file = File(rootDirectory, path)
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    fun file(path: String): File = File(rootDirectory, path)

    fun withRootBuildScript(content: String): IsolatedProjectsBuild {
        writeFile("build.gradle.kts", content)
        return this
    }

    fun withModule(name: String, buildScript: String): IsolatedProjectsBuild {
        moduleNames.add(name)
        writeFile("$name/build.gradle.kts", buildScript)
        return this
    }

    fun withSourceFile(path: String, content: String): IsolatedProjectsBuild {
        writeFile(path, content)
        return this
    }

    private fun writeSettings() {
        val includes = moduleNames.joinToString(separator = "\n") { name -> """include(":$name")""" }
        writeFile(
            path = "settings.gradle.kts",
            content = """
                dependencyResolutionManagement {
                    repositories {
                        mavenCentral()
                        mavenLocal()
                    }
                }
                rootProject.name = "ip-fixture"
                $includes
            """.trimIndent()
        )
        writeFile(path = "gradle.properties", content = GRADLE_PROPERTIES)
    }

    fun run(vararg arguments: String): BuildResult {
        writeSettings()
        return runner(arguments).build()
    }

    fun runAndFail(vararg arguments: String): BuildResult {
        writeSettings()
        return runner(arguments).buildAndFail()
    }

    private fun runner(arguments: Array<out String>): GradleRunner = GradleRunner.create()
        .withProjectDir(rootDirectory)
        .withPluginClasspath()
        .forwardOutput()
        .withArguments(arguments.toList() + listOf("--offline", "--stacktrace"))

    private companion object {
        val GRADLE_PROPERTIES = """
            org.gradle.isolated-projects=true
            org.gradle.configuration-cache=true
            org.gradle.parallel=true
            klibs.project.name=ip-fixture
            klibs.project.description=isolated projects fixture
            klibs.project.group=com.example
            klibs.project.url=https://example.com/ip-fixture
            klibs.project.version.string=1.0.0
            klibs.project.developers=makeevrserg|Makeev Roman|makeevrserg@gmail.com
            klibs.java.source=17
            klibs.java.target=17
            klibs.java.ktarget=17
            klibs.publish.name=ip-fixture
            klibs.publish.description=isolated projects fixture
            klibs.publish.repo.org=makeevrserg
            klibs.publish.repo.name=ip-fixture
            klibs.publish.license=MIT License
            klibs.publish.groupId=com.example
        """.trimIndent()
    }
}
