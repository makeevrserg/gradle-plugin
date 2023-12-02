package ru.astrainteractive.gradleplugin.multiplatform

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.SourceSetOutput
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.astrainteractive.gradleplugin.multiplatform.sourceset.JvmSourceSet

open class MinecraftMultiplatformScope(private val project: Project) {
    /**
     * Creating custom java source set  ./src/bukkitMain/kotlin/
     */
    fun bukkit() {
        JvmSourceSet(project, "bukkit").configure()
    }

    /**
     * Creating custom java source set  ./src/velocityMain/kotlin/
     */
    fun velocity() {
        JvmSourceSet(project, "velocity").configure()
    }

    /**
     * Creating custom java source set  ./src/fabricMain/kotlin/
     */
    fun fabric() {
        JvmSourceSet(project, "fabric").configure()
    }

    /**
     * Creating custom java source set  ./src/forgeMain/kotlin/
     */
    fun forge() {
        JvmSourceSet(project, "forge").configure()
    }

    /**
     * Creating all 4 sourceSets: bukkit, forge, fabric, velocity
     */
    fun defaultHierarchy() {
        bukkit()
        velocity()
        fabric()
        forge()
    }

    private fun ProjectDependency.sourceSet(name: String): SourceSetOutput {
        project.evaluationDependsOn(this.dependencyProject.path)
        val sourceSets = this
            .dependencyProject
            .extensions
            .getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer
        return sourceSets.getByName(name).output
    }

    /**
     * Extension for:
     *
     * implementation(projects.shared.bukkitMain)
     *
     * implementation(project(":shared").bukkitMain)
     */
    val ProjectDependency.bukkitMain: SourceSetOutput
        get() = this.sourceSet("bukkitMain")

    /**
     * Extension for:
     *
     * implementation(projects.shared.velocityMain)
     *
     * implementation(project(":shared").velocityMain)
     */
    val ProjectDependency.velocityMain: SourceSetOutput
        get() = this.sourceSet("velocityMain")

    /**
     * Extension for:
     *
     * implementation(projects.shared.forgeMain)
     *
     * implementation(project(":shared").forgeMain)
     */
    val ProjectDependency.forgeMain: SourceSetOutput
        get() = this.sourceSet("forgeMain")

    /**
     * Extension for:
     *
     * implementation(projects.shared.fabricMain)
     *
     * implementation(project(":shared").fabricMain)
     */
    val ProjectDependency.fabricMain: SourceSetOutput
        get() = this.sourceSet("fabricMain")

    @ExperimentalMultiplatform
    fun KotlinMultiplatformExtension.bukkitTarget() {
        createMinecraftTarget("bukkit")
    }

    @ExperimentalMultiplatform
    fun KotlinMultiplatformExtension.velocityTarget() {
        createMinecraftTarget("velocity")
    }

    @ExperimentalMultiplatform
    fun KotlinMultiplatformExtension.createMinecraftTarget(target: String) {
        val minecraftFrameworkAttribute = Attribute.of("MinecraftAttribute", String::class.java)
        jvm(target) {
            attributes.attribute(minecraftFrameworkAttribute, target)
        }
        val jvmMain = sourceSets.getByName("jvmMain")
        val targetMain = sourceSets.getByName("${target}Main")
        targetMain.dependsOn(jvmMain)
    }
}
