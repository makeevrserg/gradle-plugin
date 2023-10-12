

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

gradlePlugin {
    website.set(libs.versions.project.web.get())
    vcsUrl.set(libs.versions.project.web.get())
    description = libs.versions.project.description.get()
    plugins {
        create("minecraft.empty") {
            id = "${libs.versions.project.group.get()}.minecraft.empty"
            implementationClass = "${libs.versions.project.group.get()}.EmptyMinecraftPlugin"
            displayName = "KLibs minecraft empty stub plugin"
            description = "Empty minecraft plugin"
            tags.set(listOf("klibs"))
        }
        create("minecraft.multiplatform") {
            id = "${libs.versions.project.group.get()}.minecraft.multiplatform"
            implementationClass = "${libs.versions.project.group.get()}.multiplatform.MinecraftMultiplatformPlugin"
            displayName = "KLibs minecraft multiplatform plugin"
            description = "Minecraft multiplatform plugin"
            tags.set(listOf("klibs"))
        }
    }
}
