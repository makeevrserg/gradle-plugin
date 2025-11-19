### Minecraft plugins

### Setup resource processor

In your `build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.minecraft.resource.processor)
}

// Let's imagine, you need to provide custom libraries in your properties
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
    // The same can be done for Fabric
    fabric()
    // Forge
    forge()
    // And Velocity
    velocity()
} 
```