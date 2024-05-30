

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(libs.gradle.shadow)
    implementation(projects.buildLogic.plugins.convention)
}
fun requireProperty(name: String): String {
    return extra.get(name)
        ?.toString()
        ?: throw GradleException("Not found $name extension")
}

gradlePlugin {
    website.set(requireProperty("project.web"))
    vcsUrl.set(requireProperty("project.web"))
    description = requireProperty("project.description")
    val group = requireProperty("project.group")
    plugins {
        create("minecraft.empty") {
            id = "$group.minecraft.empty"
            implementationClass = "$group.EmptyMinecraftPlugin"
            displayName = "KLibs minecraft empty stub plugin"
            description = "Empty minecraft plugin"
            tags.set(listOf("klibs"))
        }
        create("minecraft.multiplatform") {
            id = "$group.minecraft.multiplatform"
            implementationClass = "$group.multiplatform.MinecraftMultiplatformPlugin"
            displayName = "KLibs minecraft multiplatform plugin"
            description = "Minecraft multiplatform plugin"
            tags.set(listOf("klibs"))
        }
    }
}
