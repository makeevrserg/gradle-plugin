package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo

/**
 * Set javaSource, javaTarget and kotlinJvmTarget versions
 */
class JavaVersionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val jinfo = target.requireJinfo
        target.configure<JavaPluginExtension> {
            sourceCompatibility = jinfo.jsource
            targetCompatibility = jinfo.jtarget
        }
        target.tasks
            .withType<KotlinCompile>()
            .configureEach {
                compilerOptions.jvmTarget.set(jinfo.ktarget)
            }
    }
}
