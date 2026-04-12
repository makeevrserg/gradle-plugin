package ru.astrainteractive.gradleplugin.util

import org.gradle.api.Project

internal val Project.hasAndroidKmpPlugin: Boolean
    get() = plugins
        .hasPlugin("com.android.kotlin.multiplatform.library")

internal val Project.hasAndroidLibPlugin: Boolean
    get() = plugins.hasPlugin("com.android.library")

internal val Project.hasAndroidAppPlugin: Boolean
    get() = plugins.hasPlugin("com.android.application")

internal val Project.hasAndroidPlugin: Boolean
    get() = hasAndroidAppPlugin || hasAndroidLibPlugin
