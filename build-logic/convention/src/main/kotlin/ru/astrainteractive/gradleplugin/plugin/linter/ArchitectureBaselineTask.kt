package ru.astrainteractive.gradleplugin.plugin.linter

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * Writes every current architecture violation to [baselineFile], which `architectureCheck` then accepts: old
 * code stays as it is while new code is checked. A unit without violations gets no file, and loses a stale one.
 */
@DisableCachingByDefault(because = "The baseline is a source file of the project, not a build output to cache")
internal abstract class ArchitectureBaselineTask : ArchitectureTask() {
    @get:OutputFile
    abstract val baselineFile: RegularFileProperty

    @TaskAction
    fun writeBaseline() {
        val keys = findFindings().map { finding -> finding.baselineKey }.distinct().sorted()
        val file = baselineFile.get().asFile
        if (keys.isEmpty()) {
            file.delete()
            return
        }
        file.writeText(keys.joinToString(separator = "") { key -> key + "\n" })
    }
}
