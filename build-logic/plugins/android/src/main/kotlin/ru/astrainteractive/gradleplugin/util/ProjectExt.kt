package ru.astrainteractive.gradleplugin.util

import org.gradle.api.Project

internal val Project.hasAndroidKmpPlugin: Boolean
    get() = plugins
        .hasPlugin("com.android.kotlin.multiplatform.library")
internal val Project.hasAndroidLibPlugin: Boolean
    get() = plugins.hasPlugin("com.android.library")
        .or(plugins.hasPlugin("com.android.application"))
