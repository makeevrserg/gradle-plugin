# Gradle Plugin [WIP]

This repository contains basic implementation for version catalogs and build-convention

## Setup

In your root `build.gradle.kts`
```kotlin
buildscript {
    dependencies {
        classpath("com.makeevrserg.gradleplugin:convention:<last-version>>")
    }
}
// Apply dokka root and detekt for all project
apply(plugin = "com.makeevrserg.gradleplugin.dokka.root")
apply(plugin = "com.makeevrserg.gradleplugin.detekt")
// For subprojects apply dokkaModule, pulication, infor and java.core if module have kotlin.jvm
subprojects.forEach {
    it.apply(plugin = "com.makeevrserg.gradleplugin.dokka.module")
    it.apply(plugin = "com.makeevrserg.gradleplugin.publication")
    it.apply(plugin = "com.makeevrserg.gradleplugin.root.info")
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "com.makeevrserg.gradleplugin.java.core")
    }
}
```

### Detekt

```kotlin
plugins {
    // This plugin will apply detekt plugin and it's custom detekt.yml 
    id("com.makeevrserg.gradleplugin.detekt")
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
    id("com.makeevrserg.gradleplugin.root.info")
}
// This will be applied
group = "Group from gradle.properties -> makeevrserg.project.group"
version = "Group from gradle.properties -> makeevrserg.project.version"
description = "Group from gradle.properties -> makeevrserg.project.description"
```

In your root gradle.properties

```properties
makeevrserg.project.name=GradlePlugin
makeevrserg.project.group=com.makeevrserg.gradleplugin
makeevrserg.project.version.string=0.0.2
makeevrserg.project.description=GradlePlugin for my kotlin projects
```

### Java core

```kotlin
plugins {
    // This plugin will not apply kotlin.jvm plugin by itself!!
    // This plugin will apply sourcesJar, javadocJar()
    // source/target compatibility
    // kotlin.options.jvmTarget
    // And also register java components for maven publication
    id("com.makeevrserg.gradleplugin.java.core")
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
    id("com.makeevrserg.gradleplugin.publication")
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
makeevrserg.publish.developers=makeevrserg|Makeev Roman|makeevrserg@gmail.com
```
In your local.properties
```properties
makeevrserg.OSSRH_USERNAME=OSSRH_USERNAME
makeevrserg.OSSRH_PASSWORD=OSSRH_PASSWORD
makeevrserg.SIGNING_KEY=SIGNING_KEY
makeevrserg.SIGNING_KEY_ID=SIGNING_KEY_ID
makeevrserg.SIGNING_PASSWORD=SIGNING_PASSWORD
```
## Gratitude

Thanks for moko gradle plugins and arkivanov for inspiration

- [moko mobile-multiplatform-gradle-plugin](https://github.com/icerockdev/mobile-multiplatform-gradle-plugin)
- [moko moko-gradle-plugin](https://github.com/icerockdev/moko-gradle-plugin)
- [arkivanov  gradle-setup-plugin](https://github.com/arkivanov/gradle-setup-plugin)
