package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requirePublishInfo

class PublicationPlugin : Plugin<Project> {

    @Suppress("LongMethod")
    override fun apply(target: Project) {
        val publishInfo = runCatching {
            target.requirePublishInfo
        }.getOrNull() ?: run {
            target.logger.warn("PublicationPlugin: No publish info found")
            return
        }
        val projectInfo = target.requireProjectInfo

        with(target.plugins) {
            apply("org.gradle.maven-publish")
            apply("signing")
        }

        target.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "sonatype"
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = publishInfo.ossrhUsername
                        password = publishInfo.ossrhPassword
                    }
                }
            }
            publications.create<MavenPublication>("default")
            publications.withType<MavenPublication> {
                pom {
                    this.name.set(publishInfo.libraryName)
                    this.description.set(projectInfo.description)
                    this.url.set(publishInfo.gitHubUrl)
                    groupId = publishInfo.publishGroupId
                    artifactId = target.name
                    licenses {
                        license {
                            this.name.set(publishInfo.license)
                            this.distribution.set("repo")
                            this.url.set("${publishInfo.gitHubUrl}/blob/master/LICENSE.md")
                        }
                    }

                    developers {
                        projectInfo.developersList.forEach { dev ->
                            developer {
                                id.set(dev.id)
                                name.set(dev.name)
                                email.set(dev.email)
                            }
                        }
                    }

                    scm {
                        this.connection.set(publishInfo.sshUrl)
                        this.developerConnection.set(publishInfo.sshUrl)
                        this.url.set(publishInfo.gitHubUrl)
                    }
                }
            }
        }

        target.configure<SigningExtension> {
            useInMemoryPgpKeys(publishInfo.signingKeyId, publishInfo.signingKey, publishInfo.signingPassword)
            sign(target.extensions.getByType<PublishingExtension>().publications)
        }
    }
}
