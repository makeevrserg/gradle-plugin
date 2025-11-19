plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.toolsBuild)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.android)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(projects.buildLogic.plugins.convention)
}

gradlePlugin {

    website.set(projectWeb)
    vcsUrl.set(projectWeb)
    description = projectDescription
    plugins {
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
