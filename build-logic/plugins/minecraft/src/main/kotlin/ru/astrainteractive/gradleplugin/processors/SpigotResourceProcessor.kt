package ru.astrainteractive.gradleplugin.processors

import ru.astrainteractive.gradleplugin.models.Developer

interface SpigotResourceProcessor : ResourceProcessor<SpigotResourceProcessor.SpigotProcessorInfo> {
    data class SpigotProcessorInfo(
        val main: String,
        val name: String,
        val prefix: String,
        val version: String,
        val description: String,
        val url: String,
        val author: Developer,
        val authors: List<Developer>,
        val libraries: List<String>
    ) : ResourceProcessor.ProcessorInfo
}
