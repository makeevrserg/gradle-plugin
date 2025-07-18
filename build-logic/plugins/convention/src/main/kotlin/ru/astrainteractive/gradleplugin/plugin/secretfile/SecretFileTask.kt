package ru.astrainteractive.gradleplugin.plugin.secretfile

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.encoding.Base64 as KBase64

abstract class SecretFileTask : DefaultTask() {
    @get:Input
    abstract val targetFile: Property<File>

    @get:Input
    abstract val base64: Property<String>

    @OptIn(ExperimentalEncodingApi::class)
    @TaskAction
    fun execute() {
        val file = targetFile.get()
        if (!file.exists()) file.createNewFile()
        try {
            val bytes = KBase64.decode(base64.get())
            file.writeBytes(bytes)
        } catch (e: Throwable) {
            logger.error("Could not convert base64 into $file", e)
        }
    }
}
