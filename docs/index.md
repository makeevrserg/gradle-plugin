# Gradle Plugin Suite

This repository contains a collection of useful Gradle plugins

---

## What Is This Project

This **Gradle Plugin suite** is a modular build-logic plugins library for Kotlin projects. Instead of having one
monolithic plugin, it's split into sub-plugins to provide focused functionality:

- `convention` - basic utilities for any gradle-related project
- `android` - plugins and tasks for Android projects
- `minecraft` - plugins and tasks related for Minecraft development

This modular structure allows you to pick and use only the parts of the plugin you need, keeping your builds clean and
focused.

---

## How to Use

### Define required plugins

In your `libs.version.toml`

```toml
[versions]
# klibs
klibs-gradleplugin = "<latest-version>"

[plugins]
# Core
klibs-gradle-detekt = { id = "ru.astrainteractive.gradleplugin.detekt", version.ref = "klibs-gradleplugin" }
klibs-gradle-detekt-compose = { id = "ru.astrainteractive.gradleplugin.detekt.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-module = { id = "ru.astrainteractive.gradleplugin.dokka.module", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-root = { id = "ru.astrainteractive.gradleplugin.dokka.root", version.ref = "klibs-gradleplugin" }
klibs-gradle-java-version = { id = "ru.astrainteractive.gradleplugin.java.version", version.ref = "klibs-gradleplugin" }
klibs-gradle-java-utf8 = { id = "ru.astrainteractive.gradleplugin.java.utf8", version.ref = "klibs-gradleplugin" }
klibs-gradle-rootinfo = { id = "ru.astrainteractive.gradleplugin.root.info", version.ref = "klibs-gradleplugin" }
klibs-gradle-publication = { id = "ru.astrainteractive.gradleplugin.publication", version.ref = "klibs-gradleplugin" }
# Android
klibs-gradle-android-sdk = { id = "ru.astrainteractive.gradleplugin.android.sdk", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-namespace = { id = "ru.astrainteractive.gradleplugin.android.namespace", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-compose = { id = "ru.astrainteractive.gradleplugin.android.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-sign = { id = "ru.astrainteractive.gradleplugin.android.apk.sign", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-name = { id = "ru.astrainteractive.gradleplugin.android.apk.name", version.ref = "klibs-gradleplugin" }
# Minecrat
klibs-gradle-minecraft-resource-processor = { id = "ru.astrainteractive.gradleplugin.minecraft.resource-processor", version.ref = "klibs-gradleplugin" }
```

### Setup your gradle plugins

Your root `build.gradle.kts`

```kotlin
plugins {
    // klibs - core
    alias(libs.plugins.klibs.gradle.detekt) apply false
    alias(libs.plugins.klibs.gradle.detekt.compose) apply false
    alias(libs.plugins.klibs.gradle.dokka.module) apply false
    alias(libs.plugins.klibs.gradle.dokka.root) apply false
    alias(libs.plugins.klibs.gradle.java.version) apply false
    alias(libs.plugins.klibs.gradle.java.utf8) apply false
    alias(libs.plugins.klibs.gradle.rootinfo) apply false
    alias(libs.plugins.klibs.gradle.publication) apply false
    // klibs - android
    alias(libs.plugins.klibs.gradle.android.sdk) apply false
    alias(libs.plugins.klibs.gradle.android.namespace) apply false
    alias(libs.plugins.klibs.gradle.android.compose) apply false
    alias(libs.plugins.klibs.gradle.android.apk.sign) apply false
    alias(libs.plugins.klibs.gradle.android.apk.name) apply false
    // klibs - minecraft
    alias(libs.plugins.klibs.gradle.minecraft.resource.processor) apply false
}
```

