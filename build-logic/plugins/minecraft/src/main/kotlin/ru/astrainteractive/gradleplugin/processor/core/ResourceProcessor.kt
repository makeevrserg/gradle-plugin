package ru.astrainteractive.gradleplugin.processor.core

import org.gradle.language.jvm.tasks.ProcessResources

interface ResourceProcessor<out T : ResourceProcessor.ProcessorInfo> {
    interface ProcessorInfo

    /**
     * Get default processor info
     */
    fun getProcessorInfo(): T

    /**
     * This will return default map of current processor information
     */
    fun getDefaultProperties(): Map<String, String>

    /**
     * This will configure task project.tasks.named<ProcessResources>("processResources")
     *
     * The [configuration] will be called after default configuration called
     */
    fun process(configuration: ProcessResources.() -> Unit = {})
}
