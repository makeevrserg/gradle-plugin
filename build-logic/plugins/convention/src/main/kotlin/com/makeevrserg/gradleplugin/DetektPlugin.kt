package com.makeevrserg.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("detekt-convention")
        }
    }
}
