plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    compileOnly(libs.android.toolsBuild)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.30.0")
}

gradlePlugin {
    website.set(libs.versions.project.web.get())
    vcsUrl.set(libs.versions.project.web.get())
    description = libs.versions.project.description.get()
    plugins {
        create("detekt") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.detekt.DefaultDetektPlugin"
            displayName = "KLibs detekt plugin"
            description = "Default setup for detekt plugin"
            tags.set(listOf("klibs"))
        }
        create("detekt.compose") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.detekt.ComposeDetektPlugin"
            displayName = "KLibs compose-detekt plugin"
            description = "Setup for detekt compose integration"
            tags.set(listOf("klibs"))
        }
        create("dokka.module") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.dokka.DokkaModulePlugin"
            displayName = "KLibs dokka for module configuration"
            description = "Dokka generation for project module"
            tags.set(listOf("klibs"))
        }
        create("dokka.root") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.dokka.DokkaRootPlugin"
            displayName = "KLibs Dokka for root project"
            description = "Dokka generator for root project"
            tags.set(listOf("klibs"))
        }
        create("java.core") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.JavaVersionPlugin"
            displayName = "KLibs java configuration"
            description = "Default java configuration"
            tags.set(listOf("klibs"))
        }
        create("root.info") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.ModuleInfoPlugin"
            displayName = "KLibs root info plugin"
            description = "Generates version, description for module"
            tags.set(listOf("klibs"))
        }
        create("publication") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.plugin.PublicationPlugin"
            displayName = "KLibs publication plugin"
            description = "Default pulbication plugin"
            tags.set(listOf("klibs"))
        }
    }
}
