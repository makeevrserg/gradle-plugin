package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requirePublishInfo

class PublicationPlugin : Plugin<Project> {

    @Suppress("LongMethod")
    override fun apply(target: Project) {
        val projectInfo = target.requireProjectInfo
        val publishInfo = runCatching { target.requirePublishInfo }.getOrNull()
        if (publishInfo == null) {
            target.logger.warn("PublicationPlugin: No publish info found")
            return
        }
        with(target.plugins) {
            apply("com.vanniktech.maven.publish")
        }
        target.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            publishToMavenCentral(automaticRelease = false)
            coordinates(
                groupId = publishInfo.publishGroupId,
                artifactId = target.name,
                version = projectInfo.versionString
            )
            signAllPublications()
            pom {
                this.name.set(publishInfo.libraryName)
                this.description.set(projectInfo.description)
                this.url.set(publishInfo.gitHubUrl)
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
}
