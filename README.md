# Gradle Plugin

This repository contains basic implementation for version catalogs and build-convention

## Standalone
```kotlin
plugins {
    // Standalone
    // detekt foramtter and analyzer 
    id("com.makeevrserg.gradleplugin.detekt")
}
```
## Convention plugins
```kotlin
plugins {
    // Dokka for root build.gradle.kts
    // If there's readme - it will be main page of documentation
    id("dokka-root")
    // Dokka for single module
    // You can also include README.md inside module
    // and dokka will create this README as main page
    id("dokka-convention")
    // This convention will setup common android configurations like sdk compile/kotlin options
    id("android-app-convention")
    id("android-library-convention")
}
```
