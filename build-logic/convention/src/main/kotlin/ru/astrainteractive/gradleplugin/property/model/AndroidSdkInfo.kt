package ru.astrainteractive.gradleplugin.property.model

data class AndroidSdkInfo(
    val min: Int,
    val compile: Int,
    val target: Int
)
