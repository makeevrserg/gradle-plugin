package com.makeevrserg.gradleplugin.util

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project

class GradleProperty(path: String, private val project: Project) {
    private val prop = "makeevrserg.$path"

    @Suppress("MemberNameEqualsClassName")
    private val gradleProperty: Any
        get() = project.property(prop) ?: throw GradleException("Required property $prop not defined!")

    private inline fun <reified T> withCatching(block: () -> T) = runCatching {
        block.invoke()
    }.onFailure { it.printStackTrace() }.getOrNull() ?: throw GradleException("Can't transform $prop into ${T::class}")

    val string: String
        get() = withCatching { gradleProperty.toString() }
    val integer: Int
        get() = withCatching { gradleProperty.toString().toInt() }
    val javaVersion: JavaVersion
        get() = withCatching { JavaVersion.toVersion(gradleProperty.toString().toInt()) }
    companion object {
        fun Project.gradleProperty(path: String) = GradleProperty(path, this)
    }
}
