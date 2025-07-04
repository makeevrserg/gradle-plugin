package ru.astrainteractive.gradleplugin.processor.core

import org.gradle.api.tasks.TaskProvider
import org.gradle.language.jvm.tasks.ProcessResources

internal interface ResourceProcessor<out T : ResourceProcessor.ProcessorInfo> {
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
     */
    fun process(
        task: TaskProvider<ProcessResources>,
        customProperties: Map<String, String> = emptyMap(),
    )
}
