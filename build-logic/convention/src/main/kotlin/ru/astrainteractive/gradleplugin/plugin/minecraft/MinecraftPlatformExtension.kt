package ru.astrainteractive.gradleplugin.plugin.minecraft

import ru.astrainteractive.gradleplugin.plugin.minecraft.model.ForgePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.MinecraftPlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NativePlatform
import ru.astrainteractive.gradleplugin.plugin.minecraft.model.NeoForgePlatform

open class MinecraftPlatformExtension {

    var platform: MinecraftPlatform? = null

    fun native(configure: NativePlatform.() -> Unit): NativePlatform {
        return NativePlatform().apply(configure)
    }

    fun forge(configure: ForgePlatform.() -> Unit): ForgePlatform {
        return ForgePlatform().apply(configure)
    }

    fun neoForge(configure: NeoForgePlatform.() -> Unit): NeoForgePlatform {
        return NeoForgePlatform().apply(configure)
    }
}
