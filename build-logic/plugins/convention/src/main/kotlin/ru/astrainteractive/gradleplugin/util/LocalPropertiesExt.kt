@file:Suppress("Filename")

package ru.astrainteractive.gradleplugin.util

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.util.Properties

object LocalPropertiesExt {
    /**
     * Returns project local.properties java [Properties]
     * @throws GradleException if file not found
     */
    fun Project.requireLocalProperties(): Properties {
        val secretPropsFile = project.rootProject.file("local.properties")
        return Properties().apply {
            if (!secretPropsFile.exists()) {
                project.logger.warn("No local.properties file found")
                throw GradleException("local.properties not found")
            }
            load(secretPropsFile.reader())
        }
    }

    /**
     * Returns project local.properties java [Properties] or null if not found
     */
    fun Project.findLocalProperties(): Properties? = runCatching {
        requireLocalProperties()
    }.getOrNull()

    /**
     * Returns local.properties file reference
     */
    fun Project.localPropertiesFile(): File = project.file("local.properties")
}
