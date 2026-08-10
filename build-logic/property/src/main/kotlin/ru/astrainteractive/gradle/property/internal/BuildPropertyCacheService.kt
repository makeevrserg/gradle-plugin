package ru.astrainteractive.gradle.property.internal

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.util.concurrent.ConcurrentHashMap

// Shared instead of per-project: rootProject.extensions is not allowed under isolated projects
internal abstract class BuildPropertyCacheService : BuildService<BuildServiceParameters.None> {
    private val values = ConcurrentHashMap<String, Result<String>>()

    fun computeIfAbsent(key: String, resolve: () -> Result<String>): Result<String> {
        return values.computeIfAbsent(key) { resolve() }
    }
}
