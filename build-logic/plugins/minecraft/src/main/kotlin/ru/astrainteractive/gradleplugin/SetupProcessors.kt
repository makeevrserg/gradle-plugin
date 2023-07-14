package ru.astrainteractive.gradleplugin

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.processors.fabric.FabricResourceProcessorImpl
import ru.astrainteractive.gradleplugin.processors.forge.ForgeResourceProcessorImpl
import ru.astrainteractive.gradleplugin.processors.spigot.SpigotResourceProcessorImpl
import ru.astrainteractive.gradleplugin.processors.velocity.VelocityResourceProcessorImpl

fun Project.setupSpigotProcessor() {
    SpigotResourceProcessorImpl(this).process()
}

fun Project.setupVelocityProcessor() {
    VelocityResourceProcessorImpl(this).process()
}

fun Project.setupForgeProcessor() {
    ForgeResourceProcessorImpl(this).process()
}

fun Project.setupFabricProcessor() {
    FabricResourceProcessorImpl(this).process()
}
