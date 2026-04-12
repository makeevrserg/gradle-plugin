package ru.astrainteractive.gradleplugin.util

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo

internal val Project.hasAndroidKmpPlugin: Boolean
    get() = plugins
        .hasPlugin("com.android.kotlin.multiplatform.library")

internal val Project.hasAndroidLibPlugin: Boolean
    get() = plugins.hasPlugin("com.android.library")

internal val Project.hasAndroidAppPlugin: Boolean
    get() = plugins.hasPlugin("com.android.application")

internal val Project.hasAndroidPlugin: Boolean
    get() = hasAndroidAppPlugin || hasAndroidLibPlugin

/**
 * This value will automatically create group based on folders names
 *
 * e.x :components:core:resource -> (com.example).components.core.resource
 */
val Project.hierarchyGroup: String
    get() = "${requireProjectInfo.group}.$path"
        .replace("-", ".")
        .replace(":", ".")
        .replace("..", ".")
        .lowercase()
