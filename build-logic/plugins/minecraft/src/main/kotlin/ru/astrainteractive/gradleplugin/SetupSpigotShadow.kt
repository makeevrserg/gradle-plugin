package ru.astrainteractive.gradleplugin

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo
import java.io.File

fun Project.setupSpigotShadow(
    destination: File = File(rootDir, "jars"),
    isMinimized: Boolean = true,
    configuration: ShadowJar.() -> Unit = {}
) {
    if (!destination.exists()) destination.mkdirs()

    apply(plugin = "com.github.johnrengelman.shadow")

    val projectInfo = projectInfo

    val shadowJar = tasks.named<ShadowJar>("shadowJar")
    shadowJar.configure {
        isReproducibleFileOrder = true
        mergeServiceFiles()
        dependsOn(configurations)
        archiveClassifier.set(null as String?)
        relocate("org.bstats", projectInfo.group)
        listOf(
            "kotlin",
            "org.jetbrains",
            "ru.astrainteractive.astralibs"
        ).forEach { relocate(it, "${projectInfo.group}.$it") }
        if (isMinimized) minimize()
        archiveVersion.set(projectInfo.versionString)
        archiveBaseName.set(projectInfo.name)
        destination.apply { if (!exists()) parentFile.mkdirs() }
        destination.also(destinationDirectory::set)
        configuration.invoke(this)
    }

    tasks.named<org.gradle.api.DefaultTask>("build") {
        dependsOn(shadowJar)
    }

    tasks.named<org.gradle.api.DefaultTask>("assemble") {
        dependsOn(shadowJar)
    }
}
