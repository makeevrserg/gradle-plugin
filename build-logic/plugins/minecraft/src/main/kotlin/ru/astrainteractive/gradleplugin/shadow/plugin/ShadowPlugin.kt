package ru.astrainteractive.gradleplugin.shadow.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ShadowPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            name = "setupShadow",
            type = ShadowScope::class,
            target
        )
    }
}
