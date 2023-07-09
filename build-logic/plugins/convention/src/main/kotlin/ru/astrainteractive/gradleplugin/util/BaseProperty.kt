package ru.astrainteractive.gradleplugin.util

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion

abstract class BaseProperty(protected val property: String) {

    protected inline fun <reified T> withCatching(block: () -> T) = runCatching {
        block.invoke()
    }.onFailure { it.printStackTrace() }.getOrNull() ?: throw GradleException(
        "Can't transform $property into ${T::class}"
    )

    protected abstract val anyProperty: Any

    val string: String
        get() = withCatching { anyProperty.toString() }
    val integer: Int
        get() = withCatching { anyProperty.toString().toInt() }
    val javaVersion: JavaVersion
        get() = withCatching { JavaVersion.toVersion(anyProperty.toString().toInt()) }

    companion object {
        const val BASE_PREFIX = "makeevrserg"
    }
}
