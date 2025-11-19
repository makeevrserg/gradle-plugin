## Gradle Suite properties

### Gradle Suite pre-defined properties

```properties
# JavaInfo
java.source=8
java.ktarget=11
java.target=11

# ProjectInfo
makeevrserg.project.name=MyProject
makeevrserg.project.description=This is my template project!
makeevrserg.project.group=com.example.project
makeevrserg.project.version.string=0.0.1-SNAPSHOT
makeevrserg.project.url=www.yourwebsiute.com
# Split by [,] symbol
makeevrserg.project.developers=makeevrserg|Makeev Roman|makeevrserg@gmail.com,... 

# PublishInfo
publish.name=MyProject
publish.description=My publish project description
publish.repo.org=Your-Organization
publish.repo.name=YourRepoName
publish.license=Apache-2.0
publish.groupId=io.github.yourname
```

#### Getting pre-defined properties

To get this properties in your project, use:

```kotlin
// ProjectInfo
println(requireProjectInfo)
// PublishInfo
println(requirePublishInfo)
// JavaInfo
println(requireJinfo)
```

### Gradle Suite custom properties

You can get custom-related properties

#### Getting `gradle-properties`

This will get properties from `local.properties` first. If not found, from `gradle.properties`

```kotlin
val gradleProperty: PropertyValue = target.gradleProperty("somevar")

// The [PropertyValue] looks like this
interface PropertyValue {
    val key: String
    fun getValue(): Result<String>
}
// And it contains a lot of extensions
gradleProperty.stringOrEmpty
gradleProperty.stringOrNull
gradleProperty.intOrNull
gradleProperty.requireInt
// and etc
```

#### Getting `secret-properties`

Secret properties will be taken from your `Environment` or `local.properties`

```kotlin
val secretProperty: PropertyValue = target.secretProperty("somevar")
```

> [!WARNING]  
> Environment variables doesn't allow to use `.` symbols. So for `System.Environment` the `.` replaced by `_`
>
> **Example:** In your `secret.properties` you'll have `property.first` and in `CI env` you'll have `properties_first`.

---

## Gradle Suite plugins

### Detekt

```kotlin
plugins {
    // This plugin will apply detekt plugin and it's custom detekt.yml 
    alias(libs.plugins.klibs.gradle.detekt)
    // Or if compose exists in this module use detekt-compose
    alias(libs.plugins.klibs.gradle.detekt.compose)
}
```

### Dokka

```kotlin
plugins {
    // Dokka for root build.gradle.kts
    // If there's readme - it will be main page of documentation
    alias(libs.plugins.klibs.gradle.dokka.root) apply false
    // Dokka for single module
    // You can also include README.md inside module
    // and dokka will create this README as main page
    alias(libs.plugins.klibs.gradle.dokka.module) apply false
}
```

### Java info

```kotlin
plugins {
    // This will setup project java version
    // And kotlin compile java
    alias(libs.plugins.klibs.gradle.java.version) apply false
    // This will enable options.encoding = "UTF-8"
    alias(libs.plugins.klibs.gradle.java.utf8) apply false
}
```

### Publication plugin

```kotlin
plugins {
    // This plugin will create publication to sonatype repository
    id("ru.astrainteractive.gradleplugin.publication")
}
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

This task will generate secret file from secret base64. Useful for `android.keystore` or `google-services.json`

```kotlin
tasks.register<SecretFileTask>("YOUR_TASK_NAME") {
    targetFile = file("YOUR_TARGET_FILE.txt")
    base64 = secretProperty("YOUR_KEY").requireString
}
```