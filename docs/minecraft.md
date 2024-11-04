### Minecraft plugins


### Setup Shadow
```kotlin
plugins {
    alias(libs.plugins.klibs.gradle.minecraft.shadow)
}
```


```kotlin
astraShadowJar {
    // Setup default configuration
    configureDefaults()
    // Change file destination
    destination = File("/home/server/plugins")
        .takeIf { it.exists() }
        ?: File(rootDir, "jars")

    // Here you can configure it more precisely
    requireShadowJarTask.configure {
        minimize {
            exclude(dependency(libs.exposed.jdbc.get()))
            exclude(dependency(libs.exposed.dao.get()))
        }
    }
}



```
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