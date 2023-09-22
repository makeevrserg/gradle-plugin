package ru.astrainteractive.gradleplugin.detekt

import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeDetektPlugin : Plugin<Project> by DetektPlugin(useCompose = true)
