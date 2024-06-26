package ru.astrainteractive.gradleplugin.util

import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.saveToFile
import ru.astrainteractive.gradleplugin.property.PropertyValue.Companion.baseSecretProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString
import ru.astrainteractive.gradleplugin.util.LocalPropertiesExt.localPropertiesFile
import ru.astrainteractive.gradleplugin.util.LocalPropertiesExt.requireLocalProperties
import java.io.File
import java.util.Base64

/**
 * [SecretFile] is used to decode/encode Base64 secret file
 */
class SecretFile(
    private val project: Project,
    private val file: File,
    private val key: String
) {
    /**
     * This function will convert [file] into [Base64]
     */
    fun writeToLocalProperties() {
        try {
            if (!file.exists()) {
                project.logger.info("File ${file.path} not exists")
                return
            }
            val properties = project.requireLocalProperties()
            properties.setProperty(key, Base64Util.toBase64(file))
            properties.saveToFile(org.jetbrains.kotlin.konan.file.File(project.localPropertiesFile().toPath()))
        } catch (e: Exception) {
            project.logger.error(e.stackTraceToString())
        }
    }

    fun decodeFromLocalProperties() {
        try {
            if (file.exists()) {
                project.logger.info("File ${file.path} already exists")
                return
            }
            val base64 = project.baseSecretProperty(key).requireString
            Base64Util.fromBase64(base64, file)
        } catch (e: Exception) {
            project.logger.error(e.stackTraceToString())
        }
    }
}
