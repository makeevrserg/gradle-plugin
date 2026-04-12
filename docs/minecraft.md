## Minecraft Plugins

*Depends on properties defined in [convention.md](convention.md)*

---

### Minecraft Resource Processor

**ID:** `ru.astrainteractive.gradleplugin.minecraft.resource.processor`

Provides a `minecraftProcessResource` DSL extension for processing Minecraft mod resource files. Automatically expands template variables (name, version, description, etc.) from `ProjectInfo` into resource files during build.

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.minecraft.resource.processor)
}

minecraftProcessResource {
    // Bukkit/Spigot/Paper - processes plugin.yml
    bukkit()

    // Fabric - processes fabric.mod.json
    fabric()

    // Forge - processes META-INF/mods.toml
    forge()

    // Velocity - processes velocity-plugin.json
    velocity()
}
```

### Custom properties

Each platform function accepts a `customProperties` map that merges with the defaults:

```kotlin
minecraftProcessResource {
    bukkit(
        customProperties = mapOf(
            "libraries" to listOf(
                libs.driver.h2.get(),
                libs.driver.jdbc.get(),
                libs.driver.mysql.get(),
            ).joinToString("\",\"", "[\"", "\"]")
        )
    )
}
```

### Default template variables

**Bukkit** (`plugin.yml`): `main`, `name`, `prefix`, `version`, `description`, `url`, `author`, `authors`, `libraries`

**Velocity** (`velocity-plugin.json`): `id`, `name`, `version`, `url`, `authors`, `main`

**Fabric** (`fabric.mod.json`): `version`

**Forge** (`META-INF/mods.toml`): `modId`, `version`, `description`, `displayName`, `authors`

All values are derived from `ProjectInfo` properties (`klibs.project.*`).
