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

fun requireProperty(name: String): String {
    return extra.get(name)
        ?.toString()
        ?: throw GradleException("Not found $name extension")
}

gradlePlugin {
    website.set(requireProperty("project.web"))
    vcsUrl.set(requireProperty("project.web"))
    description = requireProperty("project.description")
    val group = requireProperty("project.group")
    plugins {
        create("android.core") {
            id = "$group.android.core"
            implementationClass = "$group.AndroidSdkPlugin"
            displayName = "KLibs core android plugin"
            description = "Plugin provides basic android configuration"
            tags.set(listOf("klibs"))
        }
        create("android.compose") {
            id = "$group.android.compose"
            implementationClass = "$group.AndroidComposePlugin"
            displayName = "KLibs core android-compose plugin"
            description = "Plugin provides basic android compose setup"
            tags.set(listOf("klibs"))
        }
        create("android.apk.sign") {
            id = "$group.android.apk.sign"
            implementationClass = "$group.ApkSigningPlugin"
            displayName = "KLibs android apk sign plugin"
            description = "Plugin provides basic android sign"

            tags.set(listOf("klibs"))
        }
        create("android.apk.name") {
            id = "$group.android.apk.name"
            implementationClass = "$group.ApkNamePlugin"
            displayName = "KLibs android apk name plugin"
            description = "Plugin provides basic android naming setup"
            tags.set(listOf("klibs"))
        }
        create("android.publication") {
            id = "$group.android.publication"
            implementationClass = "$group.AndroidPublicationPlugin"
            displayName = "KLibs android publication plugin"
            description = "Plugin provides android publication"
            tags.set(listOf("klibs"))
        }
    }
}
