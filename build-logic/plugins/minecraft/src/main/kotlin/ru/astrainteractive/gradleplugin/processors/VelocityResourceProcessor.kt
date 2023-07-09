package ru.astrainteractive.gradleplugin.processors

import ru.astrainteractive.gradleplugin.models.Developer

interface VelocityResourceProcessor : ResourceProcessor<VelocityResourceProcessor.VelocityProcessorInfo> {
    data class VelocityProcessorInfo(
        val main: String,
        val name: String,
        val version: String,
        val url: String,
        val authors: List<Developer>,
        val id: String,
    ) : ResourceProcessor.ProcessorInfo
}
