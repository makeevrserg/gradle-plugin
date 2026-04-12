package ru.astrainteractive.gradle.util

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.util.Properties
import kotlin.io.path.reader

private fun File.takeIfExists(): File? {
    if (exists() || Files.isSymbolicLink(toPath())) return this
    return null
}

private fun Project.findFileOrParentFile(name: String): File? {
    val parentOrNull = parent
    val projectFileOrNull = file(name).takeIfExists()
    return when {
        projectFileOrNull != null -> projectFileOrNull
        parentOrNull != null -> parentOrNull.findFileOrParentFile(name)
        else -> rootProject.file(name).takeIfExists()
    }
}

private fun Project.requireFileOrParentFile(name: String): File {
    return findFileOrParentFile(name)
        ?: throw GradleException("No $name file found")
}

private fun File.symbolicLinkAwareReader(): InputStreamReader {
    val file = this
    val path = file.toPath()
    return if (Files.isSymbolicLink(path)) {
        Files.readSymbolicLink(path).reader()
    } else {
        file.reader()
    }
}

val Project.localProperties: Properties
    get() = Properties().apply {
        requireFileOrParentFile("local.properties")
            .symbolicLinkAwareReader()
            .run(::load)
    }

val Project.gradleProperties: Properties
    get() = Properties().apply {
        requireFileOrParentFile("gradle.properties")
            .symbolicLinkAwareReader()
            .run(::load)
    }
