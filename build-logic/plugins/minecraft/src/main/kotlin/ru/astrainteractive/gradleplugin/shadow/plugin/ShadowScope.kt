package ru.astrainteractive.gradleplugin.shadow.plugin

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo
import java.io.File

open class ShadowScope(private val project: Project) {
    val requireShadowJarTask: TaskProvider<ShadowJar>
        get() = project.tasks.named<ShadowJar>("shadowJar")

    val destination: Property<File> = project.objects.property(File::class.java)
        .convention(File(project.rootDir, "jars"))

    /**
     * Write [shadeImplementation(libs.dep)] instead of [implementation(libs.dep)]
     * to add dependency into shadowJar
     */
    val shadeImplementation = project.configurations.create("shadeImplementation")

    /**
     * Add [ShadowJar.manifest] specification
     */
    fun configureManifest() {
        val projectInfo = project.requireProjectInfo
        requireShadowJarTask.configure {
            manifest {
                attributes.putAll(
                    mapOf(
                        "Specification-Title" to projectInfo.name,
                        "Specification-Vendor" to projectInfo.developersList.first().id,
                        "Specification-Version" to projectInfo.versionString,
                        "Implementation-Title" to projectInfo.name,
                        "Implementation-Version" to projectInfo.versionString,
                        "Implementation-Vendor" to projectInfo.developersList.first().id
                    )
                )
            }
        }
    }

    fun configureDefaults() {
        val projectInfo = project.requireProjectInfo

        requireShadowJarTask.configure {
            relocate("org.bstats", projectInfo.group)
            listOf(
                "kotlin",
                "org.jetbrains",
                "ru.astrainteractive.astralibs"
            ).forEach { pattern -> relocate(pattern, "${projectInfo.group}.$pattern") }

            isReproducibleFileOrder = true
            mergeServiceFiles()
            dependsOn(project.tasks.withType<ProcessResources>())
            mustRunAfter(project.tasks.withType<ProcessResources>())
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
        project.configurations
            .getByName("implementation")
            .extendsFrom(shadeImplementation)
    }
}
