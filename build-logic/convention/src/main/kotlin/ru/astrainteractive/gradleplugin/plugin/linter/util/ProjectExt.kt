package ru.astrainteractive.gradleplugin.plugin.linter.util

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import ru.astrainteractive.gradleplugin.plugin.linter.model.UnitKind

private const val ENTRY_POINT_PATH_PREFIX = ":instances:"

private val KOTLIN_PLUGIN_IDS = listOf(
    "org.jetbrains.kotlin.jvm",
    "org.jetbrains.kotlin.multiplatform",
    "org.jetbrains.kotlin.android",
)

private val ENTRY_POINT_PLUGIN_IDS = listOf("application", "com.android.application")

private val TECHNOLOGY_UNIT_NAMES = setOf(
    "cache4k",
    "datastore",
    "exposed",
    "krate",
    "ktor",
    "okhttp",
    "retrofit",
    "room",
    "sqldelight",
)

private val PRODUCTION_CONFIGURATION_SUFFIXES = listOf("implementation", "api", "compileOnly", "runtimeOnly")

internal fun Project.hasKotlinPlugin(): Boolean {
    return KOTLIN_PLUGIN_IDS.any { pluginId -> pluginManager.hasPlugin(pluginId) }
}

private fun Project.isEntryPointByConvention(): Boolean {
    return ENTRY_POINT_PLUGIN_IDS
        .any { pluginId -> pluginManager.hasPlugin(pluginId) }
        .or(path.startsWith(ENTRY_POINT_PATH_PREFIX))
}

/**
 * The unit kind by convention: the path segment `api` or `contract`, a technology name (`data/exposed`), an
 * application plugin or a path under `:instances` for an entry point, explicit API strict for a library, a
 * context otherwise.
 */
internal fun Project.detectUnitKind(): UnitKind {
    val unitName = path.substringAfterLast(':')
    return when {
        unitName == UnitKind.API.cliName -> UnitKind.API
        unitName == UnitKind.CONTRACT.cliName -> UnitKind.CONTRACT
        unitName in TECHNOLOGY_UNIT_NAMES -> UnitKind.TECHNOLOGY
        isEntryPointByConvention() -> UnitKind.ENTRY_POINT
        hasKotlinPlugin() && explicitApiMode() == "strict" -> UnitKind.LIBRARY
        else -> UnitKind.CONTEXT
    }
}

private fun isProductionConfiguration(name: String): Boolean {
    return PRODUCTION_CONFIGURATION_SUFFIXES
        .any { suffix -> name.endsWith(suffix, ignoreCase = true) }
        .and(!name.contains("test", ignoreCase = true))
}

/**
 * Paths of the projects the production code of this project depends on; test configurations are left out.
 */
internal fun Project.productionProjectDependencies(): Set<String> {
    return configurations
        .filter { configuration -> isProductionConfiguration(configuration.name) }
        .flatMap { configuration -> configuration.dependencies.withType(ProjectDependency::class.java) }
        .map { dependency -> dependency.path }
        .filter { dependencyPath -> dependencyPath != path }
        .toSet()
}
