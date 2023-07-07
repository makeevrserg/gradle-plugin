# Gradle Plugin [WIP]

This repository contains basic implementation for version catalogs and build-convention

## Setup

In your gradle.properties define required fields

```properties
# Android
makeevrserg.android.sdk.compile=33
makeevrserg.android.sdk.min=21
makeevrserg.android.sdk.target=33
# Java
makeevrserg.java.source=8
makeevrserg.java.target=11
makeevrserg.java.ktarget=11
# Project
makeevrserg.project.name=GradlePlugin
makeevrserg.project.group=com.makeevrserg.gradleplugin
makeevrserg.project.version.string=0.0.2
makeevrserg.project.description=GradlePlugin for my kotlin projects
```

Inside your gradle file

```kotlin
plugins {
    // for full detekt support
    id("com.makeevrserg.gradleplugin.detekt")
    // to setup sdk and compile options
    id("com.makeevrserg.gradleplugin.android.core")
    // to setup java source/target and kotlin compile options
    id("com.makeevrserg.gradleplugin.java.core")
    // auto naming .apk files
    id("com.makeevrserg.gradleplugin.apk.name")
    // to setup group/version/description
    id("com.makeevrserg.gradleplugin.root.info")
    // Dokka for root build.gradle.kts
    // If there's readme - it will be main page of documentation
    id("dokka-root")
    // Dokka for single module
    // You can also include README.md inside module
    // and dokka will create this README as main page
    id("dokka-convention")
}
```

## Gratitude
Thanks for moko gradle plugins and arkivanov for inspiration
- [moko mobile-multiplatform-gradle-plugin](https://github.com/icerockdev/mobile-multiplatform-gradle-plugin)
- [moko moko-gradle-plugin](https://github.com/icerockdev/moko-gradle-plugin)
- [arkivanov  gradle-setup-plugin](https://github.com/arkivanov/gradle-setup-plugin)
