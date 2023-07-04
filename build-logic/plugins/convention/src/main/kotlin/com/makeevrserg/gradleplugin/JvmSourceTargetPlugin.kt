package com.makeevrserg.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class JvmSourceTargetPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<JavaPluginExtension> {
            sourceCompatibility = ConventionProject.SOURCE_JAVA_VERSION
            targetCompatibility = ConventionProject.TARGET_JAVA_VERSION
        }

        target.tasks
            .withType<KotlinCompile>()
            .configureEach {
                kotlinOptions.jvmTarget = ConventionProject.TARGET_JAVA_VERSION.majorVersion
            }
    }
}
