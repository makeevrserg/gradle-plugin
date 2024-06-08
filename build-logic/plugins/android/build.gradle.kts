plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

kotlin {
    jvmToolchain(11)
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
    website.set(libs.versions.project.web.get())
    vcsUrl.set(libs.versions.project.web.get())
    description = libs.versions.project.description.get()
    plugins {
        create("android.core") {
            id = "${libs.versions.project.group.get()}.android.core"
            implementationClass = "${libs.versions.project.group.get()}.AndroidSdkPlugin"
            displayName = "KLibs core android plugin"
            description = "Plugin provides basic android configuration"
            tags.set(listOf("klibs"))
        }
        create("android.namespaces") {
            id = "${libs.versions.project.group.get()}.$name"
            implementationClass = "${libs.versions.project.group.get()}.AndroidNamespacePlugin"
            displayName = "Generate android namespace"
            description = "Plugin will automatically create namespace for android extension based ot folder path"
            tags.set(listOf("klibs"))
        }
        create("android.compose") {
            id = "${libs.versions.project.group.get()}.android.compose"
            implementationClass = "${libs.versions.project.group.get()}.AndroidComposePlugin"
            displayName = "KLibs core android-compose plugin"
            description = "Plugin provides basic android compose setup"
            tags.set(listOf("klibs"))
        }
        create("android.apk.sign") {
            id = "${libs.versions.project.group.get()}.android.apk.sign"
            implementationClass = "${libs.versions.project.group.get()}.ApkSigningPlugin"
            displayName = "KLibs android apk sign plugin"
            description = "Plugin provides basic android sign"

            tags.set(listOf("klibs"))
        }
        create("android.apk.name") {
            id = "${libs.versions.project.group.get()}.android.apk.name"
            implementationClass = "${libs.versions.project.group.get()}.ApkNamePlugin"
            displayName = "KLibs android apk name plugin"
            description = "Plugin provides basic android naming setup"
            tags.set(listOf("klibs"))
        }
        create("android.publication") {
            id = "${libs.versions.project.group.get()}.android.publication"
            implementationClass = "${libs.versions.project.group.get()}.AndroidPublicationPlugin"
            displayName = "KLibs android publication plugin"
            description = "Plugin provides android publication"
            tags.set(listOf("klibs"))
        }
    }
}
