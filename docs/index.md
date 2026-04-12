# Gradle Plugin Suite

A modular collection of Gradle plugins for Kotlin projects.

---

## What Is This Project

This **Gradle Plugin suite** is a modular build-logic plugins library for Kotlin projects. Instead of having one
monolithic plugin, it's split into sub-plugins to provide focused functionality:

- `property` - property management system for `gradle.properties`, `local.properties`, and environment variables
- `convention` - core plugins for Java, Kotlin, Detekt, Dokka, publication, and more
- `android` - plugins for Android SDK, Compose, APK signing, namespacing
- `minecraft` - resource processor for Bukkit, Fabric, Forge, and Velocity

---

## How to Use

### Define required plugins

In your `libs.versions.toml`

```toml
[versions]
klibs-gradleplugin = "<latest-version>"

[plugins]
# Core
klibs-gradle-detekt = { id = "ru.astrainteractive.gradleplugin.detekt", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-module = { id = "ru.astrainteractive.gradleplugin.dokka.module", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-root = { id = "ru.astrainteractive.gradleplugin.dokka.root", version.ref = "klibs-gradleplugin" }
klibs-gradle-java-version = { id = "ru.astrainteractive.gradleplugin.java.version", version.ref = "klibs-gradleplugin" }
klibs-gradle-java-utf8 = { id = "ru.astrainteractive.gradleplugin.java.utf8", version.ref = "klibs-gradleplugin" }
klibs-gradle-rootinfo = { id = "ru.astrainteractive.gradleplugin.rootinfo", version.ref = "klibs-gradleplugin" }
klibs-gradle-publication = { id = "ru.astrainteractive.gradleplugin.publication", version.ref = "klibs-gradleplugin" }
# JavaScript
klibs-gradle-js-kobweb-resources = { id = "ru.astrainteractive.gradleplugin.js.kobweb.resources", version.ref = "klibs-gradleplugin" }
klibs-gradle-js-webpack-nosourcemaps = { id = "ru.astrainteractive.gradleplugin.js.webpack.nosourcemaps", version.ref = "klibs-gradleplugin" }
# Minecraft
klibs-gradle-minecraft-resource-processor = { id = "ru.astrainteractive.gradleplugin.minecraft.resource.processor", version.ref = "klibs-gradleplugin" }
# Android
klibs-gradle-android-java = { id = "ru.astrainteractive.gradleplugin.android.java", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-sdk = { id = "ru.astrainteractive.gradleplugin.android.sdk", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-namespace = { id = "ru.astrainteractive.gradleplugin.android.namespace", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-compose = { id = "ru.astrainteractive.gradleplugin.android.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-sign = { id = "ru.astrainteractive.gradleplugin.android.apk.sign", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-name = { id = "ru.astrainteractive.gradleplugin.android.apk.name", version.ref = "klibs-gradleplugin" }
```

### Configure your `gradle.properties`

```properties
# Java
klibs.java.source=11
klibs.java.target=11
klibs.java.ktarget=11

# Project
klibs.project.name=MyProject
klibs.project.group=com.example.project
klibs.project.version.string=1.0.0
klibs.project.description=My project description
klibs.project.url=https://github.com/username/project
klibs.project.developers=username|Full Name|email@example.com
```

### Setup your gradle plugins

Your root `build.gradle.kts`

```kotlin
plugins {
    // klibs - core
    alias(libs.plugins.klibs.gradle.detekt) apply false
    alias(libs.plugins.klibs.gradle.dokka.module) apply false
    alias(libs.plugins.klibs.gradle.dokka.root) apply false
    alias(libs.plugins.klibs.gradle.java.version) apply false
    alias(libs.plugins.klibs.gradle.java.utf8) apply false
    alias(libs.plugins.klibs.gradle.rootinfo) apply false
    alias(libs.plugins.klibs.gradle.publication) apply false
    // klibs - javascript
    alias(libs.plugins.klibs.gradle.js.kobweb.resources) apply false
    alias(libs.plugins.klibs.gradle.js.webpack.nosourcemaps) apply false
    // klibs - minecraft
    alias(libs.plugins.klibs.gradle.minecraft.resource.processor) apply false
    // klibs - android
    alias(libs.plugins.klibs.gradle.android.java) apply false
    alias(libs.plugins.klibs.gradle.android.sdk) apply false
    alias(libs.plugins.klibs.gradle.android.namespace) apply false
    alias(libs.plugins.klibs.gradle.android.compose) apply false
    alias(libs.plugins.klibs.gradle.android.apk.sign) apply false
    alias(libs.plugins.klibs.gradle.android.apk.name) apply false
}
```

---

## Documentation

- [Convention Module](convention.md) - Core plugins, properties reference, tasks, and custom property access
- [Android Plugins](android.md) - Android SDK, Compose, signing, namespace, and APK naming plugins
- [Minecraft Plugins](minecraft.md) - Resource processor for Bukkit, Fabric, Forge, and Velocity
- [Property Module](property.md) - Property management system (`PropertyValue`, lookup order, caching)

