@file:Suppress("FunctionNaming")

package ru.astrainteractive.gradleplugin.plugin.linter.plugin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import ru.astrainteractive.gradleplugin.fixture.IsolatedProjectsBuild
import java.io.File

class LinterPluginTest {
    private val javaModuleScript = """
        plugins {
            `java-library`
            id("ru.astrainteractive.gradleplugin.linter")
        }
    """.trimIndent()

    private fun jailBuild(rootDirectory: File): IsolatedProjectsBuild = IsolatedProjectsBuild(rootDirectory)
        .online()
        .withRootBuildScript("")
        .withModule(name = "jail", buildScript = javaModuleScript)
        .withSourceFile("jail/README.md", "Jails players for a sentence and frees them when it ends.")
        .withSourceFile(
            "jail/src/main/kotlin/com/example/jail/jail/usecase/JailPlayerUseCase.kt",
            """
            package com.example.jail.jail.usecase

            class JailPlayerUseCase {
                operator fun invoke() = Unit
            }
            """.trimIndent()
        )
        .withSourceFile(
            "jail/src/main/kotlin/com/example/jail/Jail.kt",
            """
            package com.example.jail

            data class Jail(val id: Int)
            """.trimIndent()
        )
        .withSourceFile(
            "jail/src/main/kotlin/com/example/jail/jail/model/Cell.kt",
            """
            package com.example.jail.jail.model

            class Cell
            """.trimIndent()
        )

    @Test
    fun GIVEN_context_with_a_loose_root_file_WHEN_architecture_checked_THEN_fails_naming_it(
        @TempDir rootDirectory: File
    ) {
        val result = jailBuild(rootDirectory).runAndFail(":jail:check")

        assertTrue(
            result.output.contains("src/main/kotlin/com/example/jail/Jail.kt: Move Jail into a kind package"),
            "the loose root file was not reported:\n${result.output}"
        )
        assertTrue(result.output.contains("[KindShape]"), "the rule was not named:\n${result.output}")
        assertTrue(
            result.output.contains("run :jail:architectureBaseline to accept"),
            "the baseline task was not suggested:\n${result.output}"
        )
    }

    @Test
    fun GIVEN_baseline_of_current_violations_WHEN_architecture_checked_THEN_passes(@TempDir rootDirectory: File) {
        val build = jailBuild(rootDirectory)
        build.run(":jail:architectureBaseline")

        build.run(":jail:architectureCheck")

        val baseline = build.file("jail/architecture-baseline.txt").readLines()
        assertEquals(listOf("KindShape\tsrc/main/kotlin/com/example/jail/Jail.kt\tJail"), baseline)
    }

    @Test
    fun GIVEN_violations_fixed_WHEN_baseline_written_again_THEN_removes_the_baseline(@TempDir rootDirectory: File) {
        val build = jailBuild(rootDirectory)
        build.run(":jail:architectureBaseline")
        val loose = build.file("jail/src/main/kotlin/com/example/jail/Jail.kt")
        val model = build.file("jail/src/main/kotlin/com/example/jail/jail/model/Jail.kt")
        model.writeText(loose.readText().replace("package com.example.jail", "package com.example.jail.jail.model"))
        loose.delete()

        build.run(":jail:architectureBaseline")

        assertTrue(!build.file("jail/architecture-baseline.txt").exists(), "an empty baseline was left behind")
    }

    @Test
    fun GIVEN_api_unit_depending_on_impl_unit_WHEN_architecture_checked_THEN_reports_the_dependency(
        @TempDir rootDirectory: File
    ) {
        val apiScript = """
            $javaModuleScript

            dependencies {
                implementation(project(":impl"))
            }
        """.trimIndent()
        val build = IsolatedProjectsBuild(rootDirectory)
            .online()
            .withRootBuildScript("")
            .withModule(name = "api", buildScript = apiScript)
            .withModule(name = "impl", buildScript = javaModuleScript)
            .withSourceFile("api/README.md", "Publishes what other contexts may call.")

        val result = build.runAndFail(":api:architectureCheck")

        assertTrue(
            result.output.contains("An api unit never depends on an impl unit") &&
                result.output.contains("[UnitDependency]"),
            "the api to impl dependency was not reported:\n${result.output}"
        )
    }

    @Test
    fun GIVEN_kotlin_library_with_explicit_api_WHEN_configured_THEN_detekt_runs_the_architecture_rules(
        @TempDir rootDirectory: File
    ) {
        val moduleScript = """
            import dev.detekt.gradle.Detekt
            import ru.astrainteractive.gradleplugin.plugin.linter.extension.LinterExtension

            plugins {
                id("org.jetbrains.kotlin.jvm")
                id("ru.astrainteractive.gradleplugin.linter")
            }

            kotlin {
                explicitApi()
            }

            val detektTask = tasks.named<Detekt>("detekt").get()
            val detektArguments = detektTask.freeCompilerArgs
            val detektConfig = detektTask.config
            val linterUnitKind = extensions.getByType<LinterExtension>().unitKind
            val detektPluginNames = configurations.getByName("detektPlugins").dependencies
                .map { dependency -> "${'$'}{dependency.group}:${'$'}{dependency.name}" }
            val linterReport = layout.buildDirectory.file("linter-report.txt")
            tasks.register("reportLinter") {
                val arguments = detektArguments
                val configFiles = detektConfig
                val unitKind = linterUnitKind
                val pluginNames = detektPluginNames
                val reportFile = linterReport
                outputs.file(reportFile)
                doLast {
                    val lines = listOf(
                        "args=" + arguments.get(),
                        "config=" + configFiles.files.map { file -> file.name },
                        "kind=" + unitKind.get(),
                        "plugins=" + pluginNames,
                    )
                    reportFile.get().asFile.writeText(lines.joinToString(separator = "\n"))
                }
            }
        """.trimIndent()
        val build = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "library", buildScript = moduleScript)

        build.run(":library:reportLinter")

        val report = build.file("library/build/linter-report.txt").readLines()
        assertEquals("args=[-Xexplicit-api=strict]", report[0])
        assertEquals("config=[detekt.yml, detekt-architecture.yml]", report[1])
        assertEquals("kind=library", report[2])
        assertTrue(report[3].contains("ru.astrainteractive.klibs.rules:detekt"), "rules missing: ${report[3]}")
    }

    private fun unitKindScript(plugins: String): String = """
        import ru.astrainteractive.gradleplugin.plugin.linter.extension.LinterExtension

        plugins {
            $plugins
            id("ru.astrainteractive.gradleplugin.linter")
        }

        val linterUnitKind = extensions.getByType<LinterExtension>().unitKind
        val kindReport = layout.buildDirectory.file("unit-kind.txt")
        tasks.register("reportUnitKind") {
            val unitKind = linterUnitKind
            val reportFile = kindReport
            outputs.file(reportFile)
            doLast { reportFile.get().asFile.writeText(unitKind.get()) }
        }
    """.trimIndent()

    @Test
    fun GIVEN_units_named_and_built_by_convention_WHEN_configured_THEN_detects_their_kind(
        @TempDir rootDirectory: File
    ) {
        val build = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "api", buildScript = unitKindScript("`java-library`"))
            .withModule(name = "contract", buildScript = unitKindScript("`java-library`"))
            .withModule(name = "server", buildScript = unitKindScript("application"))
            .withModule(name = "exposed", buildScript = unitKindScript("`java-library`"))
            .withModule(name = "jail", buildScript = unitKindScript("`java-library`"))
        val modules = listOf("api", "contract", "server", "exposed", "jail")

        build.run(*modules.map { module -> ":$module:reportUnitKind" }.toTypedArray())

        val kinds = modules.map { module -> build.file("$module/build/unit-kind.txt").readText() }
        assertEquals(listOf("api", "contract", "entry-point", "technology", "context"), kinds)
    }

    @Test
    fun GIVEN_unknown_unit_kind_WHEN_architecture_checked_THEN_fails_listing_the_kinds(@TempDir rootDirectory: File) {
        val moduleScript = """
            $javaModuleScript

            linter {
                unitKind.set("service")
            }
        """.trimIndent()
        val build = IsolatedProjectsBuild(rootDirectory)
            .online()
            .withRootBuildScript("")
            .withModule(name = "jail", buildScript = moduleScript)

        val result = build.runAndFail(":jail:architectureCheck")

        assertTrue(
            result.output.contains(
                "linter.unitKind of :jail is 'service', expected one of context, library, api, contract, entry-point"
            ),
            "the unknown kind was not reported:\n${result.output}"
        )
    }

    @Test
    fun GIVEN_context_depending_on_impl_only_in_tests_WHEN_architecture_checked_THEN_passes(
        @TempDir rootDirectory: File
    ) {
        val moduleScript = """
            $javaModuleScript

            dependencies {
                testImplementation(project(":impl"))
            }
        """.trimIndent()
        val build = IsolatedProjectsBuild(rootDirectory)
            .online()
            .withRootBuildScript("")
            .withModule(name = "jail", buildScript = moduleScript)
            .withModule(name = "impl", buildScript = javaModuleScript)
            .withSourceFile("jail/README.md", "Jails players for a sentence and frees them when it ends.")

        build.run(":jail:architectureCheck")
    }

    @Test
    fun GIVEN_root_project_with_sources_WHEN_architecture_checked_THEN_reports_them(@TempDir rootDirectory: File) {
        val build = IsolatedProjectsBuild(rootDirectory)
            .online()
            .withRootBuildScript(javaModuleScript)
            .withModule(name = "jail", buildScript = "")
            .withSourceFile("src/main/kotlin/com/example/Main.kt", "package com.example\n")

        val result = build.runAndFail(":architectureCheck")

        assertTrue(
            result.output.contains("src: Move the sources of the root project into a unit") &&
                !result.output.contains("[NestedUnit]"),
            "the root sources were not reported, or units below the root were:\n${result.output}"
        )
    }

    @Test
    fun GIVEN_kotlin_unit_with_explicit_api_warning_WHEN_configured_THEN_detekt_gets_the_warning_mode(
        @TempDir rootDirectory: File
    ) {
        val moduleScript = """
            import dev.detekt.gradle.Detekt

            plugins {
                id("org.jetbrains.kotlin.jvm")
                id("ru.astrainteractive.gradleplugin.linter")
            }

            kotlin {
                explicitApiWarning()
            }

            val detektArguments = tasks.named<Detekt>("detekt").get().freeCompilerArgs
            val argumentsReport = layout.buildDirectory.file("detekt-arguments.txt")
            tasks.register("reportDetektArguments") {
                val arguments = detektArguments
                val reportFile = argumentsReport
                outputs.file(reportFile)
                doLast { reportFile.get().asFile.writeText(arguments.get().joinToString()) }
            }
        """.trimIndent()
        val build = IsolatedProjectsBuild(rootDirectory)
            .withRootBuildScript("")
            .withModule(name = "library", buildScript = moduleScript)

        build.run(":library:reportDetektArguments")

        assertEquals("-Xexplicit-api=warning", build.file("library/build/detekt-arguments.txt").readText())
    }
}
