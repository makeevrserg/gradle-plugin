package ru.astrainteractive.gradleplugin.plugin.detekt

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.io.File

internal abstract class DetektConfigService : BuildService<DetektConfigService.Params> {
    interface Params : BuildServiceParameters {
        val content: Property<String>
        val configFile: RegularFileProperty
    }

    val configFile: File by lazy {
        parameters.configFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(parameters.content.get())
        }
    }
}
