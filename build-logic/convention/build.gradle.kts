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
            displayName = "KLibs detekt plugin"
            description = "Default setup for detekt plugin"
            tags.set(listOf("klibs"))
        }
        create("dokka.module") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.dokka.DokkaModulePlugin"
            displayName = "KLibs dokka for module configuration"
            description = "Dokka generation for project module"
            tags.set(listOf("klibs"))
        }
        create("dokka.root") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.dokka.DokkaRootPlugin"
            displayName = "KLibs Dokka for root project"
            description = "Dokka generator for root project"
            tags.set(listOf("klibs"))
        }
        create("java.version") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.JavaVersionPlugin"
            displayName = "KLibs java configuration"
            description = "Default java configuration"
            tags.set(listOf("klibs"))
        }
        create("java.utf8") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.JavaUtf8Plugin"
            displayName = "KLibs java utf8 configuration"
            description = "Default java utf8 configuration"
            tags.set(listOf("klibs"))
        }
        create("root.info") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ModuleInfoPlugin"
            displayName = "KLibs root info plugin"
            description = "Generates version, description for module"
            tags.set(listOf("klibs"))
        }
        create("publication") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.PublicationPlugin"
            displayName = "KLibs publication plugin"
            description = "Default pulbication plugin"
            tags.set(listOf("klibs"))
        }
        create("js.kobweb.resources") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.KobwebResourcesPlugin"
            displayName = "KLibs publication plugin"
            description = "Default pulbication plugin"
            tags.set(listOf("klibs"))
        }
        create("js.webpack.nosourcemaps") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.KobwebResourcesPlugin"
            displayName = "KLibs publication plugin"
            description = "Default pulbication plugin"
            tags.set(listOf("klibs"))
        }
        create("android.java") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidJavaPlugin"
            displayName = "KLibs java android plugin"
            description = "Plugin provides basic android configuration"
            tags.set(listOf("klibs"))
        }
        create("android.sdk") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidSdkPlugin"
            displayName = "KLibs core android plugin"
            description = "Plugin provides basic android configuration"
            tags.set(listOf("klibs"))
        }
        create("android.namespace") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidNamespacePlugin"
            displayName = "Generate android namespace"
            description = "Plugin will automatically create namespace for android extension based ot folder path"
            tags.set(listOf("klibs"))
        }
        create("android.compose") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.AndroidComposePlugin"
            displayName = "KLibs core android-compose plugin"
            description = "Plugin provides basic android compose setup"
            tags.set(listOf("klibs"))
        }
        create("android.apk.sign") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ApkSigningPlugin"
            displayName = "KLibs android apk sign plugin"
            description = "Plugin provides basic android sign"

            tags.set(listOf("klibs"))
        }
        create("android.apk.name") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.plugin.ApkNamePlugin"
            displayName = "KLibs android apk name plugin"
            description = "Plugin provides basic android naming setup"
            tags.set(listOf("klibs"))
        }
    }
}
