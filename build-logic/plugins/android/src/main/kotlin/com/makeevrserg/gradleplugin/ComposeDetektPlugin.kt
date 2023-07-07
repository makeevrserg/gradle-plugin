package com.makeevrserg.gradleplugin

import libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeDetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("${target.libs.versions.project.group.get()}.detekt")
        }
        target.dependencies {
            "detektPlugins"(target.libs.lint.detekt.ruleset.compose)
        }
    }
}
