package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

/**
 * Set javaSource, javaTarget and kotlinJvmTarget versions
 */
class JavaUtf8Plugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            target.tasks.withType<JavaCompile> {
                options.encoding = "UTF-8"
            }
        }
    }
}
