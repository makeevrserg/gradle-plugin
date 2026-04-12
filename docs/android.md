## Android Plugins

*Depends on properties defined in [convention.md](convention.md)*

---

### Properties

```properties
klibs.android.sdk.compile=36
klibs.android.sdk.min=24
klibs.android.sdk.target=36
klibs.android.kotlinCompilerExtensionVersion=1.5.1

klibs.java.source=11
klibs.java.target=11
klibs.java.ktarget=11
```

---

### Android SDK

**ID:** `ru.astrainteractive.gradleplugin.android.sdk`

Sets `compileSdk`, `minSdk`, and `targetSdk` from properties. Supports `com.android.application`, `com.android.library`, and `com.android.kotlin.multiplatform.library`.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.android.sdk)
}
```

---

### Android Java

**ID:** `ru.astrainteractive.gradleplugin.android.java`

Configures Java compile options and Kotlin JVM target for Android and KMP Android targets.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.android.java)
}
```

---

### Android Namespace

**ID:** `ru.astrainteractive.gradleplugin.android.namespace`

Automatically generates Android namespace from module path hierarchy. Supports both standard Android and KMP Android targets.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.android.namespace)
}
```

For a project with path `:instances:module:app` and `klibs.project.group=com.example`, the namespace will be `com.example.instances.module.app`.

---

### Android Compose

**ID:** `ru.astrainteractive.gradleplugin.android.compose`

Enables Compose build feature and sets the Kotlin compiler extension version.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.android.compose)
}
```

Reads `klibs.android.kotlinCompilerExtensionVersion` from properties.

---

### Android APK Sign

**ID:** `ru.astrainteractive.gradleplugin.android.apk.sign`

Configures debug and release signing configs using a `keystore.jks` file and secret properties.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.android.apk.sign)
}
```

Expects `keystore.jks` in the module directory. Secret properties in `local.properties`:

```properties
klibs.KEY_PASSWORD=MY_PASSWORD
klibs.KEY_ALIAS=MY_ALIAS
klibs.STORE_PASSWORD=MY_STORE_PASSWORD
```

If the keystore file or any secret is missing, signing will be skipped with a warning.

See [SecretFileTask](convention.md#secretfiletask) for generating `keystore.jks` from Base64 in CI/CD.

---

### Android APK Name

**ID:** `ru.astrainteractive.gradleplugin.android.apk.name`

Automatically names APK output files using project info.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.android.apk.name)
}
```

Output format: `{name}_{version}_{variant}.apk` (e.g. `MyApp_1.0.0_release.apk`).

Uses `klibs.project.name` and `klibs.project.version.string` from properties.
