import com.vanniktech.maven.publish.SonatypeHost
import java.io.InputStream
import java.util.Properties

buildscript {
    dependencies {
        classpath("ru.astrainteractive.gradleplugin:convention:1.4.0")
    }
}

plugins {
    `kotlin-dsl`
    id("com.vanniktech.maven.publish") version "0.30.0" apply false
    alias(libs.plugins.gradle.shadow) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.detekt")

fun findSecretProperty(property: String): Result<String> = runCatching {
    // try to get system ci property
    System.getenv(property)
        ?.let { value ->
            logger.error("Got $property property from enviroment")
            return@runCatching value
        } ?: run { logger.error("Enviroment $property property, getting from local.properties") }
    // if not ci getting from local.properties
    val properties = Properties().apply {
        val localProperties = project.rootProject.file("local.properties")
        if (!localProperties.exists()) throw GradleException("No local.properties file found")
        val inputStream: InputStream = localProperties.inputStream()
        load(inputStream)
    }
    logger.info("Got $property from local properties")
    val namedProperty = "makeevrserg.$property"
    properties.getProperty(namedProperty)
        ?: throw GradleException("Required property $namedProperty not defined!")
}

val klibs = libs

subprojects {
    val project = this
    val moduleName = project.name
    val whitelist = listOf(
        "android",
        "convention",
        "minecraft",
    )
    if (!whitelist.contains(moduleName)) return@subprojects
    project.apply(plugin = "java-gradle-plugin")
    project.apply(plugin = "com.vanniktech.maven.publish")

    project.group = klibs.versions.project.group.get()
    project.version = klibs.versions.project.version.string.get()
    project.description = klibs.versions.project.description.get()

    project.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_11
    }

    project.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
        val localProperties = project.rootProject.file("local.properties")
        if (!localProperties.exists()) return@configure
        publishToMavenCentral(
            host = SonatypeHost.CENTRAL_PORTAL,
            automaticRelease = false
        )

        signAllPublications()
        coordinates(
            groupId = klibs.versions.project.group.get(),
            artifactId = moduleName,
            version = klibs.versions.project.version.string.get()
        )
        pom {
            name.set(klibs.versions.project.name.get())
            description.set(klibs.versions.project.description.get())
            url.set(klibs.versions.project.web.get())

            licenses {
                license {
                    name.set("Apache-2.0")
                    distribution.set("repo")
                    url.set("${klibs.versions.project.web.get()}/blob/master/LICENSE.md")
                }
            }
            developers {
                developer {
                    id.set("makeevrserg")
                    name.set("Roman Makeev")
                    email.set("makeevrserg@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:ssh://github.com/makeevrserg/gradle-plugin.git")
                developerConnection.set("scm:git:ssh://github.com/makeevrserg/gradle-plugin.git")
                url.set(klibs.versions.project.web.get())
            }
        }
    }
}
