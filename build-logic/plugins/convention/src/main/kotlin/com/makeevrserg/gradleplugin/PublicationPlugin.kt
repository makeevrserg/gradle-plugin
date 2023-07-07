package com.makeevrserg.gradleplugin

import com.makeevrserg.gradleplugin.models.Developer
import com.makeevrserg.gradleplugin.util.GradleProperty.Companion.gradleProperty
import com.makeevrserg.gradleplugin.util.SecretProperty.Companion.secretProperty
import java.util.Base64
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension

class PublicationPlugin : Plugin<Project> {
    private fun Project.createOrGetJavaDoc(): Task {
        val taskName = "javadocJar"
        val foundTask = tasks.findByName(taskName)
        return foundTask ?: tasks.create<Jar>(taskName).apply {
            archiveClassifier.set("javadoc")
        }
    }

    override fun apply(target: Project) {
        with(target.plugins) {
            apply("org.gradle.maven-publish")
            apply("signing")
        }

        val libraryName: String = target.gradleProperty("publish.name").string
        val description: String = target.gradleProperty("publish.description").string
        val gitHubOrganization: String = target.gradleProperty("publish.repo.org").string
        val gitHubName: String = target.gradleProperty("publish.repo.name").string
        val license: String = target.gradleProperty("publish.license").string
        val publishGroupId: String = target.gradleProperty("publish.groupId").string
        val developersList: List<Developer> = target.gradleProperty("publish.developers").developers

        val OSSRH_USERNAME: String = target.secretProperty("OSSRH_USERNAME").string
        val OSSRH_PASSWORD: String = target.secretProperty("OSSRH_PASSWORD").string

        val gitHubUrl = "https://github.com/$gitHubOrganization/$gitHubName"
        val sshUrl = "scm:git:ssh://github.com/$gitHubOrganization/$gitHubName.git"

        target.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "sonatype"
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = OSSRH_USERNAME
                        password = OSSRH_PASSWORD
                    }
                }
            }

            publications.create<MavenPublication>("default") {
                artifact(target.createOrGetJavaDoc())
                pom {
                    this.name.set(libraryName)
                    this.description.set(description)
                    this.url.set(gitHubUrl)
                    groupId = publishGroupId
                    artifactId = target.name
                    licenses {
                        license {
                            this.name.set(license)
                            this.distribution.set("repo")
                            this.url.set("$gitHubUrl/blob/master/LICENSE.md")
                        }
                    }

                    developers {
                        developersList.forEach { dev ->
                            developer {
                                id.set(dev.id)
                                name.set(dev.name)
                                email.set(dev.email)
                            }
                        }
                    }

                    scm {
                        this.connection.set(sshUrl)
                        this.developerConnection.set(sshUrl)
                        this.url.set(gitHubUrl)
                    }
                }
            }
        }

        target.configure<SigningExtension> {
            val signingKeyId: String = target.secretProperty("SIGNING_KEY_ID").string
            val signingPassword: String = target.secretProperty("SIGNING_PASSWORD").string
            val signingKey: String = target.secretProperty("SIGNING_KEY").string
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
            sign(target.extensions.getByType<PublishingExtension>().publications)
        }
    }
}