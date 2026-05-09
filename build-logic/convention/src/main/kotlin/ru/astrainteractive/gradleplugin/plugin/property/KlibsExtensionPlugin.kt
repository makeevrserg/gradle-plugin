package ru.astrainteractive.gradleplugin.plugin.property

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.plugin.property.extension.KlibsExtension

/**
 * Registers the `klibs` project extension giving import-free access in
 * `build.gradle.kts` to all values derived from `gradle.properties`
 * (and the local `secret.properties` file).
 *
 * Apply this plugin once per module that needs the accessors:
 *
 * ```kotlin
 * plugins {
 *     id("ru.astrainteractive.gradleplugin.klibs")
 * }
 *
 * android {
 *     namespace = klibs.projectInfo.group
 *     defaultConfig {
 *         versionCode = klibs.versionCode
 *         versionName = klibs.projectInfo.versionString
 *     }
 * }
 * ```
 */
class KlibsExtensionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            "klibs",
            KlibsExtension::class.java,
            target
        )
    }
}
