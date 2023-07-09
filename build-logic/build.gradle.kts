import com.gradle.publish.PluginBundleExtension
import java.io.InputStream
import java.util.Properties


plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "0.15.0" apply false
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        withJavadocJar()
        withSourcesJar()
    }

    project.configure<PublishingExtension> {
        repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "OSSRH"
            credentials {
                username = getSecretProperty("OSSRH_USERNAME")
                password = getSecretProperty("OSSRH_PASSWORD")
            }
        }
        publications.register("mavenJava", MavenPublication::class) {
            from(project.components["java"])
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

        project.configure<PluginBundleExtension> {
            website = klibs.versions.project.web.get()
            vcsUrl = klibs.versions.project.web.get()
            description = klibs.versions.project.description.get()
            tags = listOf("kotlin")

            mavenCoordinates {
                groupId = project.group as String
                artifactId = project.name
                version = project.version as String
            }
        }
    }
}
