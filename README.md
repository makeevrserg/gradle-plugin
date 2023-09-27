![version](https://img.shields.io/maven-central/v/ru.astrainteractive.gradleplugin/convention?style=flat-square)

# Gradle Plugin

This repository contains basic implementation for version catalogs and build-convention

## Setup

In your root `build.gradle.kts`

```kotlin
buildscript {
    dependencies {
        // core gradle convention
        classpath("ru.astrainteractive.gradleplugin:convention:<latest-version>>")
        // android-specific
        classpath("ru.astrainteractive.gradleplugin:android:<latest-version>>")
    }
}
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

### Detekt

```kotlin
plugins {
    // This plugin will apply detekt plugin and it's custom detekt.yml 
    id("ru.astrainteractive.gradleplugin.detekt")
    // Or if compose exists in this module use detekt-compose
    id("ru.astrainteractive.gradleplugin.detekt-compose")
}
```

See required properties in [Java core](#java-core)

### Dokka

```kotlin
plugins {
    // Dokka for root build.gradle.kts
    // If there's readme - it will be main page of documentation
    id("dokka-root")
    // Dokka for single module
    // You can also include README.md inside module
    // and dokka will create this README as main page
    id("dokka-convention")
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
    id("ru.astrainteractive.gradleplugin.android.apk-sign")
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

### Property usage

With convention plugin added to classpath you can access to gradle.properties and secret properties, located it your
local.properties or System.env in case of CI

```kotlin
// This will take makeevrserg.somevar from gradle.properties
val gradleProperty = target.gradleProperty("somevar").javaVersion
// This will take makeevrserg.secretvar from local.properties or System.getenv if run by CI
val gradleProperty = target.secretProperty("secretvar").javaVersion
```

## Gratitude

Thanks for moko gradle plugins and arkivanov for inspiration

- [moko mobile-multiplatform-gradle-plugin](https://github.com/icerockdev/mobile-multiplatform-gradle-plugin)
- [moko moko-gradle-plugin](https://github.com/icerockdev/moko-gradle-plugin)
- [arkivanov  gradle-setup-plugin](https://github.com/arkivanov/gradle-setup-plugin)
