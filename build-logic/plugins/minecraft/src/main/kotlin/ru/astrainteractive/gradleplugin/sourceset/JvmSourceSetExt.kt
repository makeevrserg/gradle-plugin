package ru.astrainteractive.gradleplugin.sourceset

import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.SourceSetOutput

/**
 * Extension for:
 *
 * implementation(projects.shared.bukkitMain)
 *
 * implementation(project(":shared").bukkitMain)
 */
object JvmSourceSetExt {
    fun ProjectDependency.sourceSet(name: String): SourceSetOutput {
        val sourceSets = this
            .dependencyProject
            .extensions
            .getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer
        return sourceSets.getByName(name).output
    }

    val ProjectDependency.bukkitMain: SourceSetOutput
        get() = this.sourceSet("bukkitMain")

    val ProjectDependency.velocityMain: SourceSetOutput
        get() = this.sourceSet("velocityMain")

    val ProjectDependency.forgeMain: SourceSetOutput
        get() = this.sourceSet("forgeMain")

    val ProjectDependency.fabricMain: SourceSetOutput
        get() = this.sourceSet("fabricMain")
}
