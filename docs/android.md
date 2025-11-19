## Gradle Suite Android plugins

*Some functionality may be dependent on [convention.md](convention.md)*

### Gradle Suite Android pre-defined properties

```properties
android.sdk.compile=36
android.sdk.min=22
android.sdk.target=36
```

---

### Android SDK plugin

*Supports both `com.android.kotlin.multiplatform.library` and `com.android.library`*

```kotlin
plugins {
    // This will set up all android.sdk from above
    alias(libs.plugins.klibs.gradle.android.sdk)
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

See [Base64 Secret plugin section here](./convention.md) for secret file generation

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

### Android namespace plugin

*Supports both `com.android.kotlin.multiplatform.library` and `com.android.library`*

```kotlin
plugins {
    // This plugin will auto-generate namespace
    // If you project path is :instances:module:app
    // And your requireProjectInfo.group=com.example
    // You will have "com.example.instances.module.app"
    id("ru.astrainteractive.gradleplugin.android.namespace")

}
```