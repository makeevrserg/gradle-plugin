package ru.astrainteractive.gradleplugin.models

data class ProjectInfo(
    val name: String,
    val group: String,
    val versionString: String,
    val description: String,
    val url: String,
    val developersList: List<Developer>,
)
