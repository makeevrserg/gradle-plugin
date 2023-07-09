package ru.astrainteractive.gradleplugin.processors

interface ResourceProcessor<out T : ResourceProcessor.ProcessorInfo> {
    interface ProcessorInfo

    fun process()
}
