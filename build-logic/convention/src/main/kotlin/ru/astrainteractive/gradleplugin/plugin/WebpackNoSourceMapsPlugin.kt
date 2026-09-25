package ru.astrainteractive.gradleplugin.plugin

import com.varabyte.kobweb.gradle.application.extensions.app
import com.varabyte.kobweb.gradle.application.extensions.export
import com.varabyte.kobweb.gradle.core.extensions.kobwebBlock
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
        target.pluginManager.withPlugin("com.varabyte.kobweb.application") {
            target.kobwebBlock.app.export.includeSourceMap.set(false)
        }
    }
}
