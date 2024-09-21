package ru.astrainteractive.gradleplugin.shadow.plugin

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.File
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo

open class ShadowScope(private val project: Project) {
    val requireShadowJarTask: TaskProvider<ShadowJar>
        get() = project.tasks.named<ShadowJar>("shadowJar")

    val destination: Property<File> = project.objects.property(File::class.java)
        .convention(File(project.rootDir, "jars"))

    fun configureDefaults() {
        val projectInfo = project.requireProjectInfo

        requireShadowJarTask.configure {
            relocate("org.bstats", projectInfo.group)
            listOf(
                "kotlin",
                "org.jetbrains",
                "ru.astrainteractive.astralibs"
            ).forEach { relocate(it, "${projectInfo.group}.$it") }

            isReproducibleFileOrder = true
            mergeServiceFiles()
            dependsOn(configurations)
            archiveClassifier.set(null as String?)
            archiveVersion.set(projectInfo.versionString)
            archiveBaseName.set(projectInfo.name)
            destination.get().apply { if (!exists()) parentFile.mkdirs() }
            destination.get().also(destinationDirectory::set)
        }
    }

    init {
        project.apply(plugin = "io.github.goooler.shadow")

        project.tasks.named<org.gradle.api.DefaultTask>("assemble") {
            dependsOn(requireShadowJarTask)
        }

        requireShadowJarTask.configure {
            dependsOn(project.tasks.named("processResources"))
        }
    }
}