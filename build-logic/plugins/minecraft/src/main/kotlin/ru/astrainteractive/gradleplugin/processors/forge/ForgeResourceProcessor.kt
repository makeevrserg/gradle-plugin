package ru.astrainteractive.gradleplugin.processors.forge

import ru.astrainteractive.gradleplugin.processors.ResourceProcessor

interface ForgeResourceProcessor : ResourceProcessor<ForgeResourceProcessor.Info> {
    data class Info(
        val displayName: String,
        val version: String,
        val description: String,
    ) : ResourceProcessor.ProcessorInfo
}
