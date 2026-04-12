## Convention Module

The `convention` module provides a suite of Gradle plugins for standard build configurations across Kotlin, Java, Android, JavaScript, and Minecraft projects.

> See also: [Property Module](property.md) for details on the property system used by these plugins.

---

## Properties

Most plugins read configuration from `gradle.properties` using the `klibs.` prefix. Below is a complete list of properties used by the convention plugins.

### Java properties

```properties
klibs.java.source=8
klibs.java.target=11
klibs.java.ktarget=11
```

### Project properties

```properties
klibs.project.name=MyProject
klibs.project.group=com.example.project
klibs.project.version.string=0.0.1-SNAPSHOT
klibs.project.version.code=1
klibs.project.description=This is my template project!
klibs.project.url=https://www.yourwebsite.com
# Split by [,] symbol. Format: id|name|email
klibs.project.developers=makeevrserg|Makeev Roman|makeevrserg@gmail.com
```

### Publish properties

```properties
klibs.publish.name=MyProject
klibs.publish.description=My publish project description
klibs.publish.repo.org=Your-Organization
klibs.publish.repo.name=YourRepoName
klibs.publish.license=Apache-2.0
klibs.publish.groupId=io.github.yourname
```

### Android properties

```properties
klibs.android.sdk.compile=36
klibs.android.sdk.min=24
klibs.android.sdk.target=36
klibs.android.kotlinCompilerExtensionVersion=1.5.1
```

### Secret properties (local.properties or environment)

```properties
klibs.KEY_PASSWORD=MY_PASSWORD
klibs.KEY_ALIAS=MY_ALIAS
klibs.STORE_PASSWORD=MY_STORE_PASSWORD
```

> For CI/CD environment variables, replace `.` with `_` (e.g. `klibs_KEY_PASSWORD`).

### Getting pre-defined property models

```kotlin
// JInfo - Java version configuration
val jinfo = project.requireJinfo
// jinfo.jsource: JavaVersion, jinfo.jtarget: JavaVersion, jinfo.ktarget: JvmTarget

// ProjectInfo - Project metadata
val projectInfo = project.requireProjectInfo
// projectInfo.name, .group, .versionString, .description, .url, .developersList

// PublishInfo - Publication metadata
val publishInfo = project.requirePublishInfo
// publishInfo.libraryName, .description, .gitHubOrganization, .gitHubName, .license, .publishGroupId
// publishInfo.gitHubUrl, .sshUrl (derived)

// AndroidSdkInfo - Android SDK versions
val androidSdkInfo = project.requireAndroidSdkInfo
// androidSdkInfo.compile, .min, .target

// Version code
val versionCode: Int = project.requireVersionCode

// Hierarchy group - auto-generated namespace from module path
val namespace: String = project.hierarchyGroup
// e.g. :components:core:resource -> "com.example.components.core.resource"
```

---

## Plugins

### Detekt

**ID:** `ru.astrainteractive.gradleplugin.detekt`

Applies and configures [Detekt](https://detekt.dev/) static code analysis with a bundled `detekt.yml` configuration.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.detekt)
}
```

- Applies `dev.detekt` plugin
- Registers `detektFormat` task with auto-correction enabled
- Writes bundled `detekt.yml` to root project build directory
- Configures HTML reports, parallel execution, source includes/excludes
- Adds `detekt-rules-ktlint-wrapper` and `compose-rules:detekt` plugins
- Sets JVM target from `klibs.java.target`

---

### Dokka Module

**ID:** `ru.astrainteractive.gradleplugin.dokka.module`

Configures [Dokka](https://github.com/Kotlin/dokka) documentation generation for individual modules.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.dokka.module)
}
```

- Applies `org.jetbrains.dokka` plugin
- Sets module name from `project.name`
- JDK version from `klibs.java.target`
- Supports `README.md` as module documentation entry point via `DokkaTaskPartial`

---

### Dokka Root

**ID:** `ru.astrainteractive.gradleplugin.dokka.root`

Configures multi-module Dokka documentation generation for root projects.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.dokka.root)
}
```

- Applies `org.jetbrains.dokka` plugin
- Configures `DokkaMultiModuleTask` with `README.md` or `dokka.md` as entry points
- Sets module name from `project.name`

---

### Java Version

**ID:** `ru.astrainteractive.gradleplugin.java.version`

Configures Java source/target compatibility and Kotlin JVM target.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.java.version)
}
```

- Sets `JavaPluginExtension.sourceCompatibility` from `klibs.java.source`
- Sets `JavaPluginExtension.targetCompatibility` from `klibs.java.target`
- Configures all `KotlinCompile` tasks JVM target from `klibs.java.ktarget`

---

### Java UTF-8

**ID:** `ru.astrainteractive.gradleplugin.java.utf8`

Sets UTF-8 encoding for all Java compilation tasks.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.java.utf8)
}
```

- Sets `options.encoding = "UTF-8"` for all `JavaCompile` tasks

---

### Module Info

**ID:** `ru.astrainteractive.gradleplugin.rootinfo`

Applies project group, version, and description from properties.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.rootinfo)
}
```

- Sets `project.group` from `klibs.project.group`
- Sets `project.version` from `klibs.project.version.string`
- Sets `project.description` from `klibs.project.description`
- Warns if applied to a non-root project

---

### Publication

**ID:** `ru.astrainteractive.gradleplugin.publication`

Configures Maven Central publication via [Vanniktech Maven Publish](https://github.com/vanniktech/gradle-maven-publish-plugin).

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.publication)
}
```

- Applies `com.vanniktech.maven.publish` plugin
- Configures `publishToMavenCentral(automaticRelease = false)`
- Sets coordinates from `klibs.publish.groupId`, `project.name`, `klibs.project.version.string`
- Signs all publications
- Generates full POM (name, description, URL, license, developers, SCM) from project and publish properties

Required secret properties in `local.properties`:

```properties
OSSRH_USERNAME=your_username
OSSRH_PASSWORD=your_password
SIGNING_KEY=your_signing_key
SIGNING_KEY_ID=your_key_id
SIGNING_PASSWORD=your_signing_password
```

---

### Kobweb Resources

**ID:** `ru.astrainteractive.gradleplugin.js.kobweb.resources`

Manages JavaScript resources copying for [Kobweb](https://kobweb.varabyte.com/) applications.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.js.kobweb.resources)
}

jsResources {
    projectsPaths = listOf(":shared", ":frontend")
}
```

- Creates `jsResources` extension with `projectsPaths` configuration
- Registers `copyJsResources` task copying from `src/jsMain/resources/public` of referenced projects
- Wires Kobweb tasks (`KobwebStartTask`, `KobwebGenerateSiteIndexTask`, etc.) to depend on `copyJsResources`

---

### Webpack No Source Maps

**ID:** `ru.astrainteractive.gradleplugin.js.webpack.nosourcemaps`

Disables source maps in Kotlin/JS Webpack builds.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.js.webpack.nosourcemaps)
}
```

- Sets `sourceMaps = false` for all `KotlinWebpack` tasks

---

## Tasks

### SecretFileTask

A custom task that decodes a Base64 string into a file. Useful for secrets like `keystore.jks` or `google-services.json` in CI/CD.

```kotlin
tasks.register<SecretFileTask>("generateKeystore") {
    targetFile.set(file("keystore.jks"))
    base64.set(secretProperty("KEYSTORE_BASE64").requireString)
}
```



For custom property access, `PropertyValue` extensions, and caching details, see [Property Module](property.md).
