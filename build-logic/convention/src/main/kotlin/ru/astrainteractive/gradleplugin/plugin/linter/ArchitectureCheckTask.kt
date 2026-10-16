package ru.astrainteractive.gradleplugin.plugin.linter

import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import ru.astrainteractive.gradleplugin.plugin.linter.model.ArchitectureFinding

/**
 * Fails the build on every architecture violation the baseline does not list, and writes them to [reportFile].
 */
@DisableCachingByDefault(because = "The check is cheap next to the cost of storing a failing result")
internal abstract class ArchitectureCheckTask : ArchitectureTask() {
    /**
     * The baseline written by `architectureBaseline`; a missing file accepts nothing.
     */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val baseline: ConfigurableFileCollection

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    private fun readBaseline(): Set<String> = baseline.files
        .filter { file -> file.isFile }
        .flatMap { file -> file.readLines() }
        .filter { line -> line.isNotBlank() }
        .toSet()

    private fun failureMessage(findings: List<ArchitectureFinding>): String = buildString {
        appendLine("Architecture check found ${findings.size} violation(s) in ${projectPath.get()}:")
        findings.forEach { finding -> appendLine("  ${finding.describe()}") }
        val baselineTask = "${projectPath.get().removeSuffix(":")}:${LinterPlugin.BASELINE_TASK_NAME}"
        append("Fix them, or run $baselineTask to accept the current ones.")
    }

    @TaskAction
    fun check() {
        val accepted = readBaseline()
        val findings = findFindings().filterNot { finding -> finding.baselineKey in accepted }
        val report = reportFile.get().asFile
        report.parentFile.mkdirs()
        report.writeText(findings.joinToString(separator = "") { finding -> finding.describe() + "\n" })
        if (findings.isEmpty()) return
        throw GradleException(failureMessage(findings))
    }
}
