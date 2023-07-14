package ru.astrainteractive.gradleplugin.processors.fabric

import ru.astrainteractive.gradleplugin.processors.ResourceProcessor

interface FabricResourceProcessor : ResourceProcessor<FabricResourceProcessor.Info> {
    data class Info(
        val version: String,
    ) : ResourceProcessor.ProcessorInfo
}
