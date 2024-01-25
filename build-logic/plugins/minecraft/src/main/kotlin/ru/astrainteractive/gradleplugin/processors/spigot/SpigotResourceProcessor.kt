package ru.astrainteractive.gradleplugin.processors.spigot

import ru.astrainteractive.gradleplugin.model.Developer
import ru.astrainteractive.gradleplugin.processors.ResourceProcessor

interface SpigotResourceProcessor : ResourceProcessor<SpigotResourceProcessor.Info> {
    data class Info(
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
