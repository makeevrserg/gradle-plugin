package ru.astrainteractive.gradleplugin.plugin.linter.task

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.ExecOperations
import ru.astrainteractive.gradleplugin.plugin.linter.model.ArchitectureFinding
import ru.astrainteractive.gradleplugin.plugin.linter.model.UnitKind
import ru.astrainteractive.klibs.rules.structure.graph.UnitDependency
import ru.astrainteractive.klibs.rules.structure.nesting.NestedUnit
import ru.astrainteractive.klibs.rules.structure.nesting.RootSources
import ru.astrainteractive.klibs.rules.structure.readme.UnitReadme
import ru.astrainteractive.klibs.rules.structure.unit.BuildUnit
import ru.astrainteractive.klibs.rules.structure.unit.StructureChecker
import java.io.File
import javax.inject.Inject

/**
 * Finds the architecture violations of one build unit: the Konsist checks of its sources, run in a separate
 * JVM because Konsist embeds the Kotlin compiler, and the structure checks of its directory and dependencies.
 */
internal abstract class ArchitectureTask : DefaultTask() {
    @get:Classpath
    abstract val konsistClasspath: ConfigurableFileCollection

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:IgnoreEmptyDirectories
    abstract val sources: ConfigurableFileCollection

    @get:Internal
    abstract val projectDirectory: DirectoryProperty

    @get:Input
    abstract val projectPath: Property<String>

    @get:Input
    abstract val unitKind: Property<String>

    @get:Input
    @get:Optional
    abstract val rootPackage: Property<String>

    @get:Input
    abstract val rootOfBuild: Property<Boolean>

    @get:Input
    abstract val projectDependencies: SetProperty<String>

    @get:Inject
    abstract val execOperations: ExecOperations

    private fun sourceDirectories(isTest: Boolean): List<File> =
        File(projectDirectory.get().asFile, SOURCE_ROOT).listFiles().orEmpty()
            .filter { sourceSet -> sourceSet.isDirectory }
            .filter { sourceSet -> sourceSet.name.contains(TEST_MARKER, ignoreCase = true) == isTest }
            .flatMap { sourceSet -> LANGUAGE_DIRECTORIES.map { language -> File(sourceSet, language) } }
            .filter { directory -> directory.isDirectory }
            .sortedBy { directory -> directory.path }

    private fun konsistArguments(
        kind: UnitKind,
        mainDirectories: List<File>,
        reportFile: File
    ): List<String> = buildList {
        add("--project-dir")
        add(projectDirectory.get().asFile.absolutePath)
        add("--unit-kind")
        add(kind.cliName)
        add("--report")
        add(reportFile.absolutePath)
        rootPackage.orNull?.let { packageName ->
            add("--root-package")
            add(packageName)
        }
        mainDirectories.forEach { directory ->
            add("--main")
            add(directory.absolutePath)
        }
        sourceDirectories(isTest = true).forEach { directory ->
            add("--test")
            add(directory.absolutePath)
        }
    }

    private fun runKonsist(kind: UnitKind): List<ArchitectureFinding> {
        val mainDirectories = sourceDirectories(isTest = false)
        if (rootOfBuild.get() || mainDirectories.isEmpty()) return emptyList()
        val reportFile = File(temporaryDir, KONSIST_REPORT)
        execOperations.javaexec {
            classpath = konsistClasspath
            mainClass.set(KONSIST_MAIN_CLASS)
            args(konsistArguments(kind, mainDirectories, reportFile))
        }
        return reportFile.readLines()
            .filter { line -> line.isNotBlank() }
            .map { line -> ArchitectureFinding.parse(line) }
    }

    private fun runStructureChecks(kind: UnitKind): List<ArchitectureFinding> {
        val unit = BuildUnit(
            path = projectPath.get(),
            directory = projectDirectory.get().asFile,
            isEntryPoint = kind == UnitKind.ENTRY_POINT,
            isRoot = rootOfBuild.get(),
            projectDependencies = projectDependencies.get()
        )
        val checker = StructureChecker(listOf(NestedUnit(), RootSources(), UnitDependency(), UnitReadme()))
        return checker.check(unit).map { violation ->
            ArchitectureFinding(violation.rule, violation.path, violation.subject, violation.message)
        }
    }

    protected fun findFindings(): List<ArchitectureFinding> {
        val kind = UnitKind.fromCliName(unitKind.get()) ?: throw GradleException(
            "linter.unitKind of ${projectPath.get()} is '${unitKind.get()}', expected one of " +
                UnitKind.entries.joinToString { entry -> entry.cliName }
        )
        return runKonsist(kind) + runStructureChecks(kind)
    }

    private companion object {
        const val SOURCE_ROOT = "src"
        const val TEST_MARKER = "test"
        const val KONSIST_REPORT = "konsist.tsv"
        const val KONSIST_MAIN_CLASS = "ru.astrainteractive.klibs.rules.konsist.cli.KonsistCli"

        val LANGUAGE_DIRECTORIES = listOf("kotlin", "java")
    }
}
