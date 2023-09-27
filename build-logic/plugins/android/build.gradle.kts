plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
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
    isAutomatedPublishing = false
    plugins {
        create("android-core") {
            id = "${libs.versions.project.group.get()}.android.core"
            implementationClass = "${libs.versions.project.group.get()}.AndroidSdkPlugin"
        }
        create("android-compose") {
            id = "${libs.versions.project.group.get()}.android.compose"
            implementationClass = "${libs.versions.project.group.get()}.AndroidComposePlugin"
        }
        create("android-apk-sign") {
            id = "${libs.versions.project.group.get()}.android.apk-sign"
            implementationClass = "${libs.versions.project.group.get()}.ApkSigningPlugin"
        }
        create("android-apk-name") {
            id = "${libs.versions.project.group.get()}.android.apk.name"
            implementationClass = "${libs.versions.project.group.get()}.ApkNamePlugin"
        }
        create("android-publication") {
            id = "${libs.versions.project.group.get()}.android.publication"
            implementationClass = "${libs.versions.project.group.get()}.AndroidPublicationPlugin"
        }
    }
}
