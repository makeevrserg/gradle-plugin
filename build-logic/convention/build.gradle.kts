plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

dependencies {
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)

    implementation(libs.detekt.gradle)
    implementation(libs.dokka.base)
    implementation(libs.dokka.core)
    implementation(libs.dokka.gradle)
    implementation(libs.kobweb.gradle)
    implementation(libs.vaniktech)
    implementation(projects.buildLogic.property)
    implementation(projects.property)
}

gradlePlugin {
    website.set(projectWeb)
    vcsUrl.set(projectWeb)
    description = projectDescription
    plugins {
        create("detekt") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.detekt.DetektPlugin"
            displayName = "Detekt Code Analysis Plugin"
            description = "Automatically applies detekt with KLibs configuration for static code analysis with Kotlin linting rules"
            tags.set(listOf("kotlin", "static-analysis", "klibs"))
        }
        create("dokka.module") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.dokka.DokkaModulePlugin"
            displayName = "Dokka Module Documentation Plugin"
            description = "Configures Dokka for individual modules with JDK version detection and documentation settings"
            tags.set(listOf("documentation", "kotlin", "klibs"))
        }
        create("dokka.root") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.dokka.DokkaRootPlugin"
            displayName = "Dokka Root Documentation Plugin"
            description = "Configures Dokka multi-module documentation generation for root projects"
            tags.set(listOf("documentation", "kotlin", "klibs"))
        }
        create("java.version") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.JavaVersionPlugin"
            displayName = "Java Version Configuration Plugin"
            description = "Sets Java source/target compatibility and Kotlin JVM target versions from project properties"
            tags.set(listOf("java", "kotlin", "klibs"))
        }
        create("java.utf8") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.JavaUtf8Plugin"
            displayName = "Java UTF-8 Encoding Plugin"
            description = "Configures UTF-8 encoding for Java compilation tasks"
            tags.set(listOf("java", "encoding", "klibs"))
        }
        create("rootinfo") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ModuleInfoPlugin"
            displayName = "Module Info Plugin"
            description = "Applies project group, version, and description from gradle.properties to root project"
            tags.set(listOf("configuration", "klibs"))
        }
        create("publication") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.PublicationPlugin"
            displayName = "Maven Publication Plugin"
            description = "Configures Maven Central publication with POM metadata from project properties"
            tags.set(listOf("publication", "maven", "klibs"))
        }
        create("js.kobweb.resources") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.KobwebResourcesPlugin"
            displayName = "Kobweb JavaScript Resources Plugin"
            description = "Manages JavaScript resources copying for Kobweb applications"
            tags.set(listOf("javascript", "kobweb", "klibs"))
        }
        create("js.webpack.nosourcemaps") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.WebpackNoSourceMapsPlugin"
            displayName = "Webpack No Source Maps Plugin"
            description = "Disables source maps in Kotlin/JS Webpack builds for production optimization"
            tags.set(listOf("javascript", "webpack", "klibs"))
        }
        create("minecraft.resource.processor") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ResourceProcessorPlugin"
            displayName = "Minecraft Resource Processor Plugin"
            description = "Provides resource processor scope for Minecraft mod development"
            tags.set(listOf("minecraft", "resources", "klibs"))
        }
        create("android.java") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidJavaPlugin"
            displayName = "Android Java Configuration Plugin"
            description = "Configures Java/Kotlin JVM target versions for Android and Kotlin Multiplatform Android targets"
            tags.set(listOf("android", "java", "kotlin", "klibs"))
        }
        create("android.sdk") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidSdkPlugin"
            displayName = "Android SDK Configuration Plugin"
            description = "Sets compileSdk, minSdk, and targetSdk versions for Android projects from gradle.properties"
            tags.set(listOf("android", "sdk", "klibs"))
        }
        create("android.namespace") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidNamespacePlugin"
            displayName = "Android Namespace Plugin"
            description = "Automatically generates Android namespace based on module path hierarchy"
            tags.set(listOf("android", "configuration", "klibs"))
        }
        create("android.compose") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidComposePlugin"
            displayName = "Android Compose Plugin"
            description = "Enables and configures Jetpack Compose for Android projects"
            tags.set(listOf("android", "compose", "klibs"))
        }
        create("android.apk.sign") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ApkSigningPlugin"
            displayName = "Android APK Signing Plugin"
            description = "Configures APK signing for debug and release builds using keystore from gradle properties"
            tags.set(listOf("android", "signing", "klibs"))
        }
        create("android.apk.name") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ApkNamePlugin"
            displayName = "Android APK Naming Plugin"
            description = "Automatically names APK files with project name, version, and build variant"
            tags.set(listOf("android", "apk", "klibs"))
        }
    }
}
