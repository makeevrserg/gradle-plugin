package ru.astrainteractive.gradleplugin

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.processors.spigot.SpigotResourceProcessorImpl
import ru.astrainteractive.gradleplugin.processors.velocity.VelocityResourceProcessorImpl

fun Project.setupSpigotProcessor() {
    SpigotResourceProcessorImpl(this).process()
}

fun Project.setupVelocityProcessor() {
    VelocityResourceProcessorImpl(this).process()
}
