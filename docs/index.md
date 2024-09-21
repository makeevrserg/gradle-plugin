### Setup

# Gradle Plugin

This repository contains basic implementation for version catalogs and build-convention

You can use it as fork, or just setup it via plugin ↓

## Setup as plugins

<details>
  <summary> <b>(Click to expand)</b> libs.version.toml</summary>

```toml
[versions]
# klibs
klibs-gradleplugin = "<latest-version>"

[plugins]
# Core
klibs-gradle-detekt = { id = "ru.astrainteractive.gradleplugin.detekt", version.ref = "klibs-gradleplugin" }
klibs-gradle-detekt-compose = { id = "ru.astrainteractive.gradleplugin.detekt.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-root = { id = "ru.astrainteractive.gradleplugin.dokka.root", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-module = { id = "ru.astrainteractive.gradleplugin.dokka.module", version.ref = "klibs-gradleplugin" }
klibs-gradle-java-core = { id = "ru.astrainteractive.gradleplugin.java.core", version.ref = "klibs-gradleplugin" }
klibs-gradle-stub-javadoc = { id = "ru.astrainteractive.gradleplugin.stub.javadoc", version.ref = "klibs-gradleplugin" }
klibs-gradle-publication = { id = "ru.astrainteractive.gradleplugin.publication", version.ref = "klibs-gradleplugin" }
klibs-gradle-kmp-signing = { id = "ru.astrainteractive.gradleplugin.kmp-signing", version.ref = "klibs-gradleplugin" }
klibs-gradle-rootinfo = { id = "ru.astrainteractive.gradleplugin.root.info", version.ref = "klibs-gradleplugin" }
# Android
klibs-gradle-android-core = { id = "ru.astrainteractive.gradleplugin.android.core", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-compose = { id = "ru.astrainteractive.gradleplugin.android.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-sign = { id = "ru.astrainteractive.gradleplugin.android.apk.sign", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-name = { id = "ru.astrainteractive.gradleplugin.android.apk.name", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-publication = { id = "ru.astrainteractive.gradleplugin.android.publication", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-namespace = { id = "ru.astrainteractive.gradleplugin.android.namespace", version.ref = "klibs-gradleplugin" }
# Minecrat
klibs-gradle-minecraft-empty = { id = "ru.astrainteractive.gradleplugin.minecraft.empty", version.ref = "klibs-gradleplugin" }
klibs-gradle-minecraft-multiplatform = { id = "ru.astrainteractive.gradleplugin.minecraft.multiplatform", version.ref = "klibs-gradleplugin" }

```

```kotlin
plugins {
    // klibs - core
    alias(libs.plugins.klibs.gradle.detekt) apply false
    alias(libs.plugins.klibs.gradle.detekt.compose) apply false
    alias(libs.plugins.klibs.gradle.dokka.root) apply false
    alias(libs.plugins.klibs.gradle.dokka.module) apply false
    alias(libs.plugins.klibs.gradle.java.core) apply false
    alias(libs.plugins.klibs.gradle.stub.javadoc) apply false
    alias(libs.plugins.klibs.gradle.publication) apply false
    alias(libs.plugins.klibs.gradle.rootinfo) apply false
    // klibs - android
    alias(libs.plugins.klibs.gradle.android.core) apply false
    alias(libs.plugins.klibs.gradle.android.compose) apply false
    alias(libs.plugins.klibs.gradle.android.apk.sign) apply false
    alias(libs.plugins.klibs.gradle.android.apk.name) apply false
    alias(libs.plugins.klibs.gradle.android.publication) apply false
    alias(libs.plugins.klibs.gradle.android.namespace) apply false
    // klibs - minecraft
    alias(libs.plugins.klibs.gradle.minecraft.empty) apply false
    alias(libs.plugins.klibs.gradle.minecraft.multiplatform) apply false
}
```

</details>

## Setup as classpath

<details>
  <summary><b>(Click to expand)</b> Root build.gradle.kts</summary>

**libs.version.toml**

In your root `build.gradle.kts`

```kotlin
buildscript {
    dependencies {
        // core gradle convention
        classpath("ru.astrainteractive.gradleplugin:convention:<latest-version>>")
        // android-specific
        classpath("ru.astrainteractive.gradleplugin:android:<latest-version>>")
        // minecraft-specific
        classpath("ru.astrainteractive.gradleplugin:minecraft:<latest-version>>")
    }
}
// If you are too lazy to define it in each gralde.kts, just paste it in root

// Apply dokka root and detekt for all project
apply(plugin = "ru.astrainteractive.gradleplugin.dokka.root")
apply(plugin = "ru.astrainteractive.gradleplugin.detekt")
// For subprojects apply dokkaModule, pulication, infor and java.core if module have kotlin.jvm
subprojects.forEach {
    it.apply(plugin = "ru.astrainteractive.gradleplugin.dokka.module")
    it.apply(plugin = "ru.astrainteractive.gradleplugin.publication")
    it.apply(plugin = "ru.astrainteractive.gradleplugin.root.info")
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
}
```

</details>