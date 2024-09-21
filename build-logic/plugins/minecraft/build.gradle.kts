

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
        create("minecraft.resource-processor") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.processors.plugin.ResourceProcessorPlugin"
            displayName = "KLibs minecraft resource processor plugin"
            description = "Minecraft resource processor plugin"
            tags.set(listOf("klibs"))
        }
        create("minecraft.shadow") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.shadow.plugin.ShadowPlugin"
            displayName = "KLibs minecraft shadow plugin"
            description = "Minecraft shadow plugin"
            tags.set(listOf("klibs"))
        }
    }
}
