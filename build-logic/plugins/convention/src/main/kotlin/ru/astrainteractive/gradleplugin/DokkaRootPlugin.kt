package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import java.io.File

class DokkaRootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("org.jetbrains.dokka")
        }

        target.tasks.withType<DokkaMultiModuleTask> {
            val paths = listOf("README.md", "dokka.md")
                .map(project::file)
                .filter(File::exists)
                .map(File::getName)

            includes.from(paths)
            moduleName.set(target.name)
        }

        target.tasks.withType<DokkaTask>().configureEach {
            dokkaSourceSets.configureEach {
                perPackageOption {
                    reportUndocumented.set(false)
                }
            }
        }
    }
}
