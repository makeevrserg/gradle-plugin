package ru.astrainteractive.gradleplugin.util

import java.io.File
import java.util.Base64

object Base64Util {
    /**
     * Encode [File] into [Base64] string
     */
    fun toBase64(file: File): String {
        val bytes = file.readBytes()
        return Base64.getEncoder().encodeToString(bytes)
    }

    /**
     * Converts [Base64] string into [output] [File]
     */
    fun fromBase64(base64: String, output: File): File {
        val bytes = Base64.getDecoder().decode(base64)
        if (!output.exists()) {
            output.parentFile.mkdirs()
            output.createNewFile()
        }
        output.writeBytes(bytes)
        return output
    }
}
