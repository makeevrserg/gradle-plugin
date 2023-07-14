package ru.astrainteractive.gradleplugin.processors.velocity

import ru.astrainteractive.gradleplugin.models.Developer
import ru.astrainteractive.gradleplugin.processors.ResourceProcessor

interface VelocityResourceProcessor : ResourceProcessor<VelocityResourceProcessor.Info> {
    data class Info(
        val main: String,
        val name: String,
        val version: String,
        val url: String,
        val authors: List<Developer>,
        val id: String,
    ) : ResourceProcessor.ProcessorInfo
}
