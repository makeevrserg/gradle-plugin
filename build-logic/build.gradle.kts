import java.io.InputStream
import java.util.Properties

buildscript {
    dependencies {
        classpath("ru.astrainteractive.gradleplugin:convention:1.3.4")
    }
}

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.2" apply false
    alias(libs.plugins.gradle.shadow) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.detekt")

fun getSecretProperty(property: String): String {
    // try to get system ci property
    System.getenv(property)
        ?.let { value ->
            logger.error("Got $property property from enviroment")
            return value
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
    return properties.getProperty(namedProperty)
        ?: throw GradleException("Required property $namedProperty not defined!")
}

val klibs = libs

subprojects {
    val project = this
    val moduleName = project.name

    project.apply(plugin = "org.gradle.maven-publish")
    project.apply(plugin = "signing")
    project.apply(plugin = "java-gradle-plugin")
    project.apply(plugin = "com.gradle.plugin-publish")

    project.group = klibs.versions.project.group.get()
    project.version = klibs.versions.project.version.string.get()
    project.description = klibs.versions.project.description.get()

    project.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_11
        withJavadocJar()
        withSourcesJar()
    }

    project.configure<PublishingExtension> {
        val localProperties = project.rootProject.file("local.properties")
        if (!localProperties.exists()) return@configure
        repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "OSSRH"
            credentials {
                username = getSecretProperty("OSSRH_USERNAME")
                password = getSecretProperty("OSSRH_PASSWORD")
            }
        }

//        publications.register("mavenJava", MavenPublication::class) {
//            from(project.components["java"])
//        }

        publications.withType<MavenPublication> {
            pom {
                name.set(klibs.versions.project.name.get())
                description.set(klibs.versions.project.description.get())
                url.set(klibs.versions.project.web.get())

                groupId = klibs.versions.project.group.get()
                artifactId = moduleName

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

        project.configure<SigningExtension> {
            val signingKey = getSecretProperty("SIGNING_KEY")
            val signingKeyId = getSecretProperty("SIGNING_KEY_ID")
            val signingPassword = getSecretProperty("SIGNING_PASSWORD")
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
            sign(publications)
        }
        val signingTasks = project.tasks.withType<Sign>()
        project.tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(signingTasks)
        }
    }
}
