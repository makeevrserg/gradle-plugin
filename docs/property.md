## Property Module

The `property` module provides a flexible property management system for accessing configuration values from `gradle.properties`, `local.properties`, and environment variables.

---

## PropertyValue Interface

All property access methods return a `PropertyValue`:

```kotlin
interface PropertyValue {
    val key: String
    fun getValue(): Result<String>
}
```

---

## Accessing Properties

### Gradle Property (with `klibs.` prefix)

Prepends `klibs.` to the path. Lookup order: `local.properties` -> `gradle.properties` -> Gradle providers.

```kotlin
val property: PropertyValue = project.klibsGradleProperty("project.name")
// Resolves key: klibs.project.name
```

### Gradle Property (raw key)

Uses the key as-is. Same lookup order.

```kotlin
val property: PropertyValue = project.gradleProperty("some.custom.key")
// Resolves key: some.custom.key
```

### Secret Property (with `klibs.` prefix)

Prepends `klibs.` to the path. Lookup order: environment variable -> `local.properties`.

```kotlin
val secret: PropertyValue = project.klibsSecretProperty("KEY_PASSWORD")
// Resolves key: klibs.KEY_PASSWORD
// Environment variable: klibs_KEY_PASSWORD (dots replaced with underscores)
```

### Secret Property (raw key)

Uses the key as-is. Same lookup order.

```kotlin
val secret: PropertyValue = project.secretProperty("CUSTOM_SECRET")
// Resolves key: CUSTOM_SECRET
```

> **Environment variables:** Since `System.getenv` doesn't support dots, all `.` in the key are replaced with `_` when looking up environment variables.

---

## PropertyValue Extensions

### String

```kotlin
property.stringOrNull      // String? - null if not found
property.stringOrEmpty      // String - empty string if not found
property.requireString      // String - throws if not found
```

### Integer

```kotlin
property.intOrNull          // Int? - null if not found or not parseable
property.requireInt         // Int - throws if not found
```

### JavaVersion / JvmTarget

```kotlin
property.requireJavaVersion // JavaVersion - throws if not found
property.requireJvmTarget   // JvmTarget - throws if not found
```

---

## Caching

Properties can be cached per-project using `asCached`:

```kotlin
val cached: PropertyValue = property.asCached(project.extensions)
```

This stores the resolved value in the project's `ExtensionContainer` to avoid repeated file lookups. Values are cached per key - including "not found" results.

---

## Property File Lookup

Both `local.properties` and `gradle.properties` are resolved by walking up the project hierarchy:

1. Check current project directory
2. Check parent project directory
3. Check root project directory

This means a single `gradle.properties` at the root is sufficient for all subprojects. Symbolic links are supported.

---

## Integration with Convention Module

The `property` module is used by the `convention` module to provide typed property models. See [Convention Module](convention.md) for the full list of `klibs.*` property keys and pre-defined models like `JInfo`, `ProjectInfo`, `PublishInfo`, and `AndroidSdkInfo`.
