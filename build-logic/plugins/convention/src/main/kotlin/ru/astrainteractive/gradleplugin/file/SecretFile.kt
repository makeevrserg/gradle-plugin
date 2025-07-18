package ru.astrainteractive.gradleplugin.file

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.saveToFile
import ru.astrainteractive.gradleplugin.property.baseSecretProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString
import java.io.File
import java.util.Base64
import java.util.Properties
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.encoding.Base64 as KBase64
import org.jetbrains.kotlin.konan.file.File as KFile

/**
 * [SecretFile] is used to decode/encode Base64 secret file
 * TODO convert to plugin
 */
class SecretFile(
    private val project: Project,
    private val file: File,
    private val key: String
) {

    private fun requireLocalProperties(): Properties {
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
     * This function will convert [file] into [Base64]
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun writeToLocalProperties() {
        try {
            if (!file.exists()) error("File ${file.path} not exists")
            requireLocalProperties()
                .apply {
                    setProperty(key, KBase64.encode(file.readBytes()))
                    saveToFile(
                        file = project.file("local.properties")
                            .toPath()
                            .let(::KFile)
                    )
                }
        } catch (e: Exception) {
            project.logger.error("Could not save local properties for key $key", e)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodeFromLocalProperties() {
        try {
            if (file.exists()) error("File ${file.path} already exists")
            project.baseSecretProperty(key)
                .requireString
                .let(KBase64::decode)
                .run(file::writeBytes)
        } catch (e: Exception) {
            project.logger.error("Could not write local properties for key $key", e)
            project.logger.error(e.stackTraceToString())
        }
    }
}
