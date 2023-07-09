package ru.astrainteractive.gradleplugin.models

import org.gradle.api.JavaVersion

data class JInfo(
    val ktarget: JavaVersion,
    val jtarget: JavaVersion,
    val jsource: JavaVersion
)
