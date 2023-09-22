package ru.astrainteractive.gradleplugin.detekt

import org.gradle.api.Plugin
import org.gradle.api.Project

class DefaultDetektPlugin : Plugin<Project> by DetektPlugin(useCompose = false)
