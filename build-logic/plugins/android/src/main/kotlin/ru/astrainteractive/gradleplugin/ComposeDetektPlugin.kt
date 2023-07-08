package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeDetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("ru.astrainteractive.gradleplugin.detekt")
        }
        target.dependencies {
            "detektPlugins"("com.twitter.compose.rules:detekt:0.0.26")
        }
    }
}
