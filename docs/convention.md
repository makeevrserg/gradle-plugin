### Property usage

With convention plugin added to classpath you can access to gradle.properties and secret properties, located it your
local.properties or System.env in case of CI

```kotlin
// This will take makeevrserg.somevar from gradle.properties
val gradleProperty = target.gradleProperty("somevar").javaVersion
// This will take makeevrserg.secretvar from local.properties or System.getenv if run by CI
val gradleProperty = target.secretProperty("secretvar").javaVersion
```

But there's one problem. Environment variables doesn't allow to use '.' symbols.
So when declare your properties in CI just replace '.' with '_'.

Then in your gradle.properties you'll have `var.properties.first` and in CI env you'll have `var_properties_first`.

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

### Base64 Secret plugin

```kotlin
tasks.register<SecretFileTask>("YOUR_TASK_NAME") {
    targetFile = file("YOUR_TARGET_FILE.txt")
    base64 = "SOME_BASE64_TEXT"
}
```