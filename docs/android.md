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

### Android namespace plugin

```kotlin
plugins {
    // This plugin will auto-generate namespace
    id("ru.astrainteractive.gradleplugin.android.namespace")

}
```