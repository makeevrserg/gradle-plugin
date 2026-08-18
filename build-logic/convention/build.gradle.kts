plugins {
    `kotlin-dsl`
    id("klibs.module")
}

abstract class FabricLoomJavaVersionRule : ComponentMetadataRule {
    override fun execute(ctx: ComponentMetadataContext) {
        ctx.details.allVariants {
            attributes {
                attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
            }
        }
    }
}

val testPluginDependencies = configurations.dependencyScope("testPluginDependencies")
val testPluginClasspath = configurations.resolvable("testPluginClasspath") {
    extendsFrom(testPluginDependencies.get())
}

dependencies {
    components { withModule<FabricLoomJavaVersionRule>("net.fabricmc:fabric-loom") }

    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.forge.gradle)
    compileOnly(libs.fabric.loom) { isTransitive = false }

    implementation(libs.detekt.gradle)
    implementation(libs.dokka.base)
    implementation(libs.dokka.core)
    implementation(libs.dokka.gradle)
    implementation(libs.kobweb.gradle)
    implementation(libs.vaniktech)
    implementation(projects.property)

    testImplementation(gradleTestKit())
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // The plugins read Kotlin Gradle plugin types, which stay compileOnly in production
    add(testPluginDependencies.name, libs.kotlin.gradle)
}

tasks.pluginUnderTestMetadata {
    pluginClasspath.from(testPluginClasspath)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

gradlePlugin {
    website.set(klibs.url)
    vcsUrl.set(klibs.url)
    description = klibs.description
    plugins {
        create("detekt") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.detekt.DetektPlugin"
            displayName = "Detekt Code Analysis Plugin"
            description =
                "Automatically applies detekt with KLibs configuration for static code analysis with Kotlin linting rules"
            tags.set(listOf("kotlin", "static-analysis", "klibs"))
        }
        create("dokka") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.dokka.DokkaPlugin"
            displayName = "Dokka Documentation Plugin"
            description = "Applies Dokka with JDK version detection and shared documentation settings"
            tags.set(listOf("documentation", "kotlin", "klibs"))
        }
        create("java.version") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.JavaVersionPlugin"
            displayName = "Java Version Configuration Plugin"
            description = "Sets Java source/target compatibility and Kotlin JVM target versions from project properties"
            tags.set(listOf("java", "kotlin", "klibs"))
        }
        create("java.utf8") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.JavaUtf8Plugin"
            displayName = "Java UTF-8 Encoding Plugin"
            description = "Configures UTF-8 encoding for Java compilation tasks"
            tags.set(listOf("java", "encoding", "klibs"))
        }
        create("rootinfo") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.ModuleInfoPlugin"
            displayName = "Module Info Plugin"
            description = "Applies project group, version, and description from gradle.properties to root project"
            tags.set(listOf("configuration", "klibs"))
        }
        create("publication") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.PublicationPlugin"
            displayName = "Maven Publication Plugin"
            description = "Configures Maven Central publication with POM metadata from project properties"
            tags.set(listOf("publication", "maven", "klibs"))
        }
        create("js.kobweb.resources") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.KobwebResourcesPlugin"
            displayName = "Kobweb JavaScript Resources Plugin"
            description = "Manages JavaScript resources copying for Kobweb applications"
            tags.set(listOf("javascript", "kobweb", "klibs"))
        }
        create("js.webpack.nosourcemaps") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.WebpackNoSourceMapsPlugin"
            displayName = "Webpack No Source Maps Plugin"
            description = "Disables source maps in Kotlin/JS Webpack builds for production optimization"
            tags.set(listOf("javascript", "webpack", "klibs"))
        }
        create("minecraft.platform") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.minecraft.MinecraftPlatformPlugin"
            displayName = "Minecraft Platform Plugin"
            description =
                "Configures the Minecraft platform toolchain (native/Forge/NeoForge) via a single `minecraftPlatform { }` DSL block"
            tags.set(listOf("minecraft", "forge", "neoforge", "fabric", "klibs"))
        }
        create("minecraft.resource.processor") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.ResourceProcessorPlugin"
            displayName = "Minecraft Resource Processor Plugin"
            description = "Provides resource processor scope for Minecraft mod development"
            tags.set(listOf("minecraft", "resources", "klibs"))
        }
        create("android.java") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.AndroidJavaPlugin"
            displayName = "Android Java Configuration Plugin"
            description =
                "Configures Java/Kotlin JVM target versions for Android and Kotlin Multiplatform Android targets"
            tags.set(listOf("android", "java", "kotlin", "klibs"))
        }
        create("android.sdk") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.AndroidSdkPlugin"
            displayName = "Android SDK Configuration Plugin"
            description = "Sets compileSdk, minSdk, and targetSdk versions for Android projects from gradle.properties"
            tags.set(listOf("android", "sdk", "klibs"))
        }
        create("android.namespace") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.AndroidNamespacePlugin"
            displayName = "Android Namespace Plugin"
            description = "Automatically generates Android namespace based on module path hierarchy"
            tags.set(listOf("android", "configuration", "klibs"))
        }
        create("android.compose") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.AndroidComposePlugin"
            displayName = "Android Compose Plugin"
            description = "Enables and configures Jetpack Compose for Android projects"
            tags.set(listOf("android", "compose", "klibs"))
        }
        create("android.apk.sign") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.ApkSigningPlugin"
            displayName = "Android APK Signing Plugin"
            description = "Configures APK signing for debug and release builds using keystore from gradle properties"
            tags.set(listOf("android", "signing", "klibs"))
        }
        create("android.apk.name") {
            id = "${klibs.group}.$name"
            implementationClass = "${klibs.group}.plugin.ApkNamePlugin"
            displayName = "Android APK Naming Plugin"
            description = "Automatically names APK files with project name, version, and build variant"
            tags.set(listOf("android", "apk", "klibs"))
        }
    }
}
