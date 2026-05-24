## Minecraft Plugins

*Depends on properties defined in [convention.md](convention.md)*

---

### Minecraft Platform Plugin

**ID:** `ru.astrainteractive.gradleplugin.minecraft.platform`

Configures the Minecraft platform toolchain via a single `minecraftPlatform { }` DSL block. Exactly one platform must be selected per module. Supported platforms: **native** (vanilla/Fabric via fabric-loom), **Forge** (ForgeGradle), **NeoForge** (NeoGradle).

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.minecraft.platform)
}

minecraftPlatform {
    // Native / vanilla – applies fabric-loom and adds Mojang mappings
    platform = native {
        version = "1.21.4"
    }

    // Forge – applies net.minecraftforge.gradle and adds forge as compileOnly
    platform = forge {
        version = "1.20.1-47.3.0"
        useLocal = false   // true: resolve from local .gradle/mavenizer/repo instead
    }

    // NeoForge – applies net.neoforged.gradle.userdev and adds neoforge as compileOnly
    platform = neoForge {
        version = "21.4.167"
        useLocal = false   // true: resolve from local .gradle/repositories/ng_dummy_ng instead
    }
}
```

#### Platform details

| Platform   | Gradle plugin applied          | Dependency added                                   |
|------------|--------------------------------|----------------------------------------------------|
| `native`   | `fabric-loom`                  | `com.mojang:minecraft:<version>` + Mojang mappings |
| `forge`    | `net.minecraftforge.gradle`    | `net.minecraftforge:forge:<version>` (compileOnly) |
| `neoForge` | `net.neoforged.gradle.userdev` | `net.neoforged:neoforge:<version>` (compileOnly)   |

> **Note:** `platform` must be assigned inside the block; omitting it causes a build error.

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

    // NeoForge - processes META-INF/mods.toml
    neoForge()

    // Velocity - processes velocity-plugin.json
    velocity()
}
```

### Custom properties

Each platform function accepts a `customProperties: Map<String, String>` that merges with the defaults:

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

**Forge** (`META-INF/mods.toml`): `mod_id`, `mod_name`, `mod_version`, `mod_authors`, `mod_description`

**NeoForge** (`META-INF/mods.toml`): `neo_version`, `mod_id`, `mod_name`, `mod_license`, `mod_version`, `mod_authors`, `mod_description`

> `neo_version` and `mod_license` are passed through as literal placeholder strings by default and must be overridden via `customProperties` if your template references them directly.

All other values are derived from `ProjectInfo` properties (`klibs.project.*`).
