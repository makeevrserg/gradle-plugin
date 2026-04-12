package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

/**
 * Disable source maps in webpack builds
 */
class WebpackNoSourceMapsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.withType<KotlinWebpack>()
            .configureEach { sourceMaps = false }
    }
}
