![version](https://img.shields.io/maven-central/v/ru.astrainteractive.gradleplugin/convention?style=flat-square)

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
# klibs - core
klibs-gradle-detekt = { id = "ru.astrainteractive.gradleplugin.detekt", version.ref = "klibs-gradleplugin" }
klibs-gradle-detekt-compose = { id = "ru.astrainteractive.gradleplugin.detekt.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-root = { id = "ru.astrainteractive.gradleplugin.dokka.root", version.ref = "klibs-gradleplugin" }
klibs-gradle-dokka-module = { id = "ru.astrainteractive.gradleplugin.dokka.module", version.ref = "klibs-gradleplugin" }
klibs-gradle-java-core = { id = "ru.astrainteractive.gradleplugin.java.core", version.ref = "klibs-gradleplugin" }
klibs-gradle-stub-javadoc = { id = "ru.astrainteractive.gradleplugin.stub.javadoc", version.ref = "klibs-gradleplugin" }
klibs-gradle-publication = { id = "ru.astrainteractive.gradleplugin.publication", version.ref = "klibs-gradleplugin" }
klibs-gradle-kmp-signing = { id = "ru.astrainteractive.gradleplugin.kmp-signing", version.ref = "klibs-gradleplugin" }
klibs-gradle-rootinfo = { id = "ru.astrainteractive.gradleplugin.root.info", version.ref = "klibs-gradleplugin" }
# klibs - android
klibs-gradle-android-core = { id = "ru.astrainteractive.gradleplugin.android.core", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-compose = { id = "ru.astrainteractive.gradleplugin.android.compose", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-sign = { id = "ru.astrainteractive.gradleplugin.android.apk.sign", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-apk-name = { id = "ru.astrainteractive.gradleplugin.android.apk.name", version.ref = "klibs-gradleplugin" }
klibs-gradle-android-publication = { id = "ru.astrainteractive.gradleplugin.android.publication", version.ref = "klibs-gradleplugin" }
# klibs - minecraft
klibs-gradle-minecraft-empty = { id = "ru.astrainteractive.gradleplugin.minecraft.empty", version.ref = "klibs-gradleplugin" }
klibs-gradle-minecraft-multiplatform = { id = "ru.astrainteractive.gradleplugin.minecraft.multiplatform", version.ref = "klibs-gradleplugin" }

```

**Root build.gradle.kts**

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

### Property usage

With convention plugin added to classpath you can access to gradle.properties and secret properties, located it your
local.properties or System.env in case of CI

```kotlin
// This will take makeevrserg.somevar from gradle.properties
val gradleProperty = target.gradleProperty("somevar").javaVersion
// This will take makeevrserg.secretvar from local.properties or System.getenv if run by CI
val gradleProperty = target.secretProperty("secretvar").javaVersion
```

### Detekt

```kotlin
plugins {
    // This plugin will apply detekt plugin and it's custom detekt.yml 
    id("ru.astrainteractive.gradleplugin.detekt")
    // Or if compose exists in this module use detekt-compose
    id("ru.astrainteractive.gradleplugin.detekt.compose")
}
```

See required properties in [Java core](#java-core)

### Dokka

```kotlin
plugins {
    // Dokka for root build.gradle.kts
    // If there's readme - it will be main page of documentation
    id("ru.astrainteractive.gradleplugin.dokka.root")
    // Dokka for single module
    // You can also include README.md inside module
    // and dokka will create this README as main page
    id("ru.astrainteractive.gradleplugin.dokka.module")
}
```

See required properties in [Java core](#java-core)

### Root info

```kotlin
plugins {
    id("ru.astrainteractive.gradleplugin.root.info")
}
// This will be applied
group = "Group from gradle.properties -> makeevrserg.project.group"
version = "Group from gradle.properties -> makeevrserg.project.version"
description = "Group from gradle.properties -> makeevrserg.project.description"
```

In your root gradle.properties

```properties
makeevrserg.project.name=GradlePlugin
makeevrserg.project.group=ru.astrainteractive.gradleplugin
makeevrserg.project.version.string=0.0.2
makeevrserg.project.description=GradlePlugin for my kotlin projects
makeevrserg.project.developers=makeevrserg|Makeev Roman|makeevrserg@gmail.com
```

### Java core

```kotlin
plugins {
    // This plugin will not apply kotlin.jvm plugin by itself!!
    // This plugin will apply sourcesJar, javadocJar()
    // source/target compatibility
    // kotlin.options.jvmTarget
    // And also register java components for maven publication
    id("ru.astrainteractive.gradleplugin.java.core")
}
```

In your gradle.properties

```properties
makeevrserg.java.source=8
makeevrserg.java.target=11
makeevrserg.java.ktarget=11
```

### Publication plugin

```kotlin
plugins {
    // This plugin will create publication to sonatype repository
    id("ru.astrainteractive.gradleplugin.publication")
}
```

In your gradle.properties

```properties
makeevrserg.publish.name=AstraLibs
makeevrserg.publish.groupId=ru.astrainteractive.astralibs
makeevrserg.publish.description=Core utilities for spigot development
makeevrserg.publish.repo.org=Astra-Interactive
makeevrserg.publish.repo.name=AstraLibs
makeevrserg.publish.license=Custom
```

In your local.properties

```properties
OSSRH_USERNAME=OSSRH_USERNAME
OSSRH_PASSWORD=OSSRH_PASSWORD
SIGNING_KEY=SIGNING_KEY
SIGNING_KEY_ID=SIGNING_KEY_ID
SIGNING_PASSWORD=SIGNING_PASSWORD
```

## Android plugins

### Android detekt-compose

This plugin is dependent on [core-detekt](#detekt)

```kotlin
plugins {
    id("ru.astrainteractive.gradleplugin.detekt")
}    
```

### Android sdk plugin

```kotlin
plugins {
    // This plugin will add sdk source/target/min
    id("ru.astrainteractive.gradleplugin.android.core")
}    
```

### Android compose plugin

```kotlin
plugins {
    // this will enable buildFeatures.compose
    // and will set composeOptions.kotlinCompilerExtensionVersion
    id("ru.astrainteractive.gradleplugin.android.compose")
}    
```

In your gradle.properties

```properties
makeevrserg.android.kotlinCompilerExtensionVersion=1.5.1
```

### Android apk sign plugin

```kotlin
plugins {
    // this will create default sign config for apk
    // keystore.jks will be taken from current project folder
    // if no keystore.jks - no signing configs will be applied
    id("ru.astrainteractive.gradleplugin.android.apk.sign")
}    
```

In your local.properties

```properties
KEY_PASSWORD=MY_PASSWORD
KEY_ALIAS=MY_ALIAS
STORE_PASSWORD=MY_STORE_PASSWORD
```

### Android publication

```kotlin
plugins {
    // This plugin will take release sources for publish
    id("ru.astrainteractive.gradleplugin.android.publication")

}
```

## Minecraft plugins [Experimental]

```kotlin
plugins {
    id("ru.astrainteractive.gradleplugin.minecraft.multiplatform")
}
minecraftMultiplatform {
    // Create sourceSets one by one
    bukkit()
    velocity()
    fabric()
    forge()
    // Or create all at once
    defaultHierarchy()
}
dependencies {
    implementation("some.shared.dependency")
    // Bukkit
    "bukkitMainCompileOnly"("spigot.only.dependency")
    // This will be added to bukkit source set
    "bukkitMainCompileOnly"(libs.minecraft.paper.api)
    // Shared test dependencies
    testImplementation(libs.tests.kotlin.test)
    // Bukkit test dependencies
    "bukkitTestImplementation"("com.github.seeseemelk:MockBukkit-v1.16:1.0.0")
}
```

Thus, project structure will look like below:

    ├── shared   
    │   ├── src/bukkitMain/kotlin
    │   ├── src/velocityMain/kotlin
    │   ├── src/velocityTest/kotlin
    │   └── src/main/kotlin
    ├── bukkit
    │   └── src/main/kotlin      

To enable bukkit dependencies from `:shared` into `:bukkit` make this:

```kotlin
// In your :bukkit
minecraftMultiplatform {
    dependencies {
        implementation(projects.shared.bukkitMain)
        // Or this
        implementation(project(":shared").bukkitMain)
    }
}
```

Don't forget gradle modules could sync in different order. So in some cases you are required to write:

```kotlin 
// Inside :spigot->build.gradle.kts
// When using `implementation(projects.shared.bukkitMain)` 
// - evaluationDependsOn applied automatically
evaluationDependsOn(":modules:my-shared-module")
```

Also can be configured with KotlinMultiplatform(Experimental)
```kotlin
kotlin {
    jvm {
        withJava()
    }
    minecraftMultiplatform {
        bukkitTarget()
        velocityTarget()
    }
    sourceSets {
        val bukkitMain by getting {
            ...
        }
    }
}

```

## Gratitude

Thanks for moko gradle plugins and arkivanov for inspiration

- [moko mobile-multiplatform-gradle-plugin](https://github.com/icerockdev/mobile-multiplatform-gradle-plugin)
- [moko moko-gradle-plugin](https://github.com/icerockdev/moko-gradle-plugin)
- [arkivanov  gradle-setup-plugin](https://github.com/arkivanov/gradle-setup-plugin)
