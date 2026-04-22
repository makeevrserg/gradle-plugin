package ru.astrainteractive.gradleplugin.plugin

import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradle.property.api.gradleProperty
import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo
import ru.astrainteractive.gradleplugin.property.util.requirePublishInfo

class PublicationPlugin : Plugin<Project> {
    private fun Project.canSignPublication(): Boolean {
        val hasEnvMavenUsername = gradleProperty("ORG_GRADLE_PROJECT_mavenCentralUsername")
            .getValue()
            .isSuccess
        val hasGradlePropertyUsername = gradleProperty("mavenCentralUsername")
            .getValue()
            .isSuccess
        return hasEnvMavenUsername || hasGradlePropertyUsername
    }

    @Suppress("LongMethod")
    override fun apply(target: Project) {
        val canSignPublication = target.canSignPublication()
        val projectInfo = target.requireProjectInfo
        val publishInfo = runCatching { target.requirePublishInfo }.getOrNull()
        if (publishInfo == null) {
            target.logger.warn("PublicationPlugin: No publish info found")
            return
        }
        with(target.plugins) {
            apply("com.vanniktech.maven.publish")
        }
        target.configure<MavenPublishBaseExtension> {
            publishToMavenCentral(
                automaticRelease = true,
                validateDeployment = DeploymentValidation.VALIDATED
            )
            coordinates(
                groupId = publishInfo.publishGroupId,
                artifactId = target.name,
                version = projectInfo.versionString
            )
            if (canSignPublication) signAllPublications()
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
