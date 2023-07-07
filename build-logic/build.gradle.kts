import java.io.InputStream
import java.util.Properties
plugins {
    `kotlin-dsl`
    id("org.gradle.maven-publish")
    id("signing")
    id("java-gradle-plugin")
}

fun getSecretProperty(property: String): String {
    val property = "makeevrserg.$property"
    // try to get system ci property
    System.getenv(property)?.let { return it }
    // if not ci getting from local.properties
    val properties = Properties().apply {
        val localProperties = project.rootProject.file("local.properties")
        if (!localProperties.exists()) throw GradleException("No local.properties file found")
        val inputStream: InputStream = localProperties.inputStream()
        load(inputStream)
    }
    return properties.getProperty(property) ?: throw GradleException("Required property $property not defined!")
}

val klibs = libs

subprojects {
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "signing")
    apply(plugin = "java-gradle-plugin")

    group = klibs.versions.project.group.get()
    version = klibs.versions.project.version.string.get()

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        withJavadocJar()
        withSourcesJar()
    }

    configure<PublishingExtension> {
        repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "OSSRH"
            credentials {
                username = getSecretProperty("OSSRH_USERNAME")
                password = getSecretProperty("OSSRH_PASSWORD")
            }
        }

        publications.register("mavenJava", MavenPublication::class) {
            from(components["java"])
            pom {
                name.set("GradlePlugin")
                description.set("Custom gradle plugin for my libraries and projects")
                url.set("https://github.com/makeevrserg/gradle-plugin")
                groupId = klibs.versions.project.group.get()
                artifactId = klibs.versions.project.group.get()

                licenses {
                    license {
                        name.set("Apache-2.0")
                        distribution.set("repo")
                        url.set("https://github.com/makeevrserg/gradle-plugin/blob/master/LICENSE.md")
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
                    url.set("https://github.com/makeevrserg/gradle-plugin")
                }
            }
        }
    }

    configure<SigningExtension> {
        val signingKey = getSecretProperty("SIGNING_KEY")
        val signingKeyId = getSecretProperty("SIGNING_KEY_ID")
        val signingPassword = getSecretProperty("SIGNING_PASSWORD")
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}
