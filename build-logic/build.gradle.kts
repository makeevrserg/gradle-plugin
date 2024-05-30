plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1" apply false
    alias(libs.plugins.gradle.shadow) apply false
    id("ru.astrainteractive.gradleplugin.detekt") version "1.1.2" apply true
}

apply(plugin = "ru.astrainteractive.gradleplugin.detekt")
apply(from = "./property.gradle.kts")

fun requireProperty(name: String): String {
    return extra.get(name)
        ?.toString()
        ?: throw GradleException("Not found $name extension")
}

subprojects {
    val project = this
    val moduleName = project.name
    project.apply(plugin = "org.gradle.maven-publish")
    project.apply(plugin = "signing")
    project.apply(plugin = "java-gradle-plugin")
    project.apply(plugin = "com.gradle.plugin-publish")

    project.group = requireProperty("project.group")
    project.version = requireProperty("project.version")
    project.description = requireProperty("project.description")

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
                username = requireProperty("secret.ossrhUsername")
                password = requireProperty("secret.osshPassword")
            }
        }

//        publications.register("mavenJava", MavenPublication::class) {
//            from(project.components["java"])
//        }

        publications.withType<MavenPublication> {
            pom {
                name.set(requireProperty("project.name"))
                description.set(requireProperty("project.description"))
                url.set(requireProperty("project.web"))

                groupId = requireProperty("project.group")
                artifactId = moduleName

                licenses {
                    license {
                        name.set("Apache-2.0")
                        distribution.set("repo")
                        url.set("${requireProperty("project.web")}/blob/master/LICENSE.md")
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
                    url.set(requireProperty("project.web"))
                }
            }
        }

        project.afterEvaluate {
            project.configure<SigningExtension> {
                val signingKey = requireProperty("secret.signingKey")
                val signingKeyId = requireProperty("secret.signingKeyId")
                val signingPassword = requireProperty("secret.signingPassword")
                useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
                sign(publications)
                sign(project.extensions.getByType<PublishingExtension>().publications)
            }
        }

        val signingTasks = project.tasks.withType<Sign>()
        project.tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(signingTasks)
        }
    }
}
