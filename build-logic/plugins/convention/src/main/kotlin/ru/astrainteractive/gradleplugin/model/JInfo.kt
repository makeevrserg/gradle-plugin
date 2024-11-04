package ru.astrainteractive.gradleplugin.model

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

data class JInfo(
    val ktarget: JvmTarget,
    val jtarget: JavaVersion,
    val jsource: JavaVersion
)
