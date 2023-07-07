plugins {
    `kotlin-dsl`
    id("org.gradle.maven-publish")
    id("signing")
    id("java-gradle-plugin")
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.toolsBuild)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.android)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(projects.buildLogic.plugins.convention)
}

gradlePlugin {
    plugins {
        create("android-core") {
            id = "${libs.versions.project.group.get()}.android.core"
            implementationClass = "${libs.versions.project.group.get()}.AndroidSdkPlugin"
        }
        create("android-apk-name") {
            id = "${libs.versions.project.group.get()}.android.apk.name"
            implementationClass = "${libs.versions.project.group.get()}.ApkNamePlugin"
        }
        create("android-publication") {
            id = "${libs.versions.project.group.get()}.android.publication"
            implementationClass = "${libs.versions.project.group.get()}.AndroidPublicationPlugin"
        }
        create("detekt-compose") {
            id = "${libs.versions.project.group.get()}.detekt.compose"
            implementationClass = "${libs.versions.project.group.get()}.ComposeDetektPlugin"
        }
    }
}