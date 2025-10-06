### Minecraft plugins

### Setup resource processor 
```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.minecraft.resource.processor)
}

minecraftProcessResource {
    spigotResourceProcessor.process {
        expand("{some_prop}" to 10)
    }
}
```