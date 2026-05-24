package ru.astrainteractive.gradleplugin.plugin.minecraft.model

sealed interface MinecraftPlatform

class NativePlatform : MinecraftPlatform {
    var version: String = ""
}

class ForgePlatform : MinecraftPlatform {
    var version: String = ""
    var useLocal: Boolean = false
}

class NeoForgePlatform : MinecraftPlatform {
    var version: String = ""
    var useLocal: Boolean = false
}
