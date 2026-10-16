package ru.astrainteractive.gradleplugin.plugin.linter

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

/**
 * The explicit API mode the Kotlin extension sets, as the compiler argument value (`strict`, `warning`), or
 * `null` when it is off.
 *
 * Call it only when a Kotlin plugin is applied: this file is the one place that loads Kotlin Gradle plugin
 * types, which a build without Kotlin does not have.
 */
internal fun Project.explicitApiMode(): String? =
    when (extensions.findByType(KotlinBaseExtension::class.java)?.explicitApi) {
        ExplicitApiMode.Strict -> "strict"
        ExplicitApiMode.Warning -> "warning"
        else -> null
    }
