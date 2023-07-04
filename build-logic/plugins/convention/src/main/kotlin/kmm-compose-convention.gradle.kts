@file:Suppress("UnusedPrivateMember")

import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}
kotlin {
    android()
    sourceSets {
        val commonMain by getting
        val androidMain by getting
    }
}
android {
    defaultConfig {
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}
