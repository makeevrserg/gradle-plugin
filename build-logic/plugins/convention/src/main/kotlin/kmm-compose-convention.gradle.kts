@file:Suppress("UnusedPrivateMember")

import com.makeevrserg.gradleplugin.ConventionProject
import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}
java {
    java.sourceCompatibility = ConventionProject.JAVA_VERSION
    java.targetCompatibility = ConventionProject.JAVA_VERSION
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = ConventionProject.JAVA_VERSION.majorVersion
}
kotlin {
    android()
    sourceSets {
        val commonMain by getting
        val androidMain by getting
    }
}
android {
    compileSdk = libs.versions.project.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.project.sdk.min.get().toInt()
        targetSdk = libs.versions.project.sdk.target.get().toInt()
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = ConventionProject.JAVA_VERSION
        targetCompatibility = ConventionProject.JAVA_VERSION
    }
}
