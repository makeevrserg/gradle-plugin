package ru.astrainteractive.gradleplugin.property

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.util.Properties
import kotlin.io.path.reader

private fun File.takeIfIsLinkOrExist(): File? {
    if (exists() || Files.isSymbolicLink(toPath())) return this
    return null
}

internal fun Project.fileOrParentFile(name: String): File? {
    val parentOrNull = parent
    val projectFileOrNull = file(name).takeIfIsLinkOrExist()
    return when {
        projectFileOrNull != null -> projectFileOrNull
        parentOrNull != null -> parentOrNull.fileOrParentFile(name)
        else -> rootProject.file(name).takeIfIsLinkOrExist()
    }
}

internal fun Project.requireFileOrParentFile(name: String): File {
    return fileOrParentFile(name)
        ?: throw GradleException("No local.properties file found")
}

internal fun Properties.fromFile(file: File): Properties {
    val reader = file.let { file ->
        val path = file.toPath()
        if (Files.isSymbolicLink(path)) {
            Files.readSymbolicLink(path).reader()
        } else {
            file.reader()
        }
    }
    load(reader)
    return this
}
