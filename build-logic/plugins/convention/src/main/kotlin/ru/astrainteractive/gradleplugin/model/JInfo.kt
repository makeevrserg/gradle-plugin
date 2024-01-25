package ru.astrainteractive.gradleplugin.model

import org.gradle.api.JavaVersion

data class JInfo(
    val ktarget: JavaVersion,
    val jtarget: JavaVersion,
    val jsource: JavaVersion
)
