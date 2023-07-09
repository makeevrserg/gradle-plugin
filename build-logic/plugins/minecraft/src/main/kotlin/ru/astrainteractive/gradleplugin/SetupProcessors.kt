package ru.astrainteractive.gradleplugin

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.processors.SpigotResourceProcessorImpl

fun Project.setupSpigotProcessor() {
    SpigotResourceProcessorImpl(this).process()
}

fun Project.setupVelocityProcessor() {
    SpigotResourceProcessorImpl(this).process()
}
