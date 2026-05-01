import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

plugins {
    `kotlin-dsl`
    alias(libs.plugins.vaniktech) apply false
    id("ru.astrainteractive.gradleplugin.detekt") version "2.0.1" apply true
}

private fun requireProperty(key: String): String {
    return rootProject.property(key)
        ?.toString()
        ?: throw GradleException("Could not find property $key")
}

private fun canSignPublication(): Boolean {
    val hasEnvMavenUsername = System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") != null
    val hasGradlePropertyUsername = providers.gradleProperty("mavenCentralUsername").isPresent
    return hasEnvMavenUsername || hasGradlePropertyUsername
}

data class ProjectConfiguration(
    val projectName: String = requireProperty("project.name"),
    val projectDescription: String = requireProperty("project.description"),
    val projectGroup: String = requireProperty("project.group"),
    val projectWeb: String = requireProperty("project.web"),
    val projectVersionString: String = requireProperty("project.version.string")
)

val projectConfiguration = ProjectConfiguration()

allprojects {
    extensions.add("projectName", projectConfiguration.projectName)
    extensions.add("projectDescription", projectConfiguration.projectDescription)
    extensions.add("projectGroup", projectConfiguration.projectGroup)
    extensions.add("projectWeb", projectConfiguration.projectWeb)
    extensions.add("projectVersionString", projectConfiguration.projectVersionString)
}

subprojects {
    val project = this
    project.apply(plugin = "java-gradle-plugin")
    project.apply(plugin = "com.vanniktech.maven.publish")

    project.group = projectConfiguration.projectGroup
    project.version = projectConfiguration.projectVersionString
    project.description = projectConfiguration.projectDescription

    afterEvaluate {
        configure<KotlinBaseExtension> {
            jvmToolchain(17)
        }
    }

    project.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_17
    }

    project.configure<MavenPublishBaseExtension> {
        publishToMavenCentral(
            automaticRelease = true,
            validateDeployment = DeploymentValidation.VALIDATED
        )
        coordinates(
            groupId = projectConfiguration.projectGroup,
            artifactId = project.name,
            version = projectConfiguration.projectVersionString
        )
        pom {
            name.set(projectConfiguration.projectName)
            description.set(projectConfiguration.projectDescription)
            url.set(projectConfiguration.projectWeb)

            licenses {
                license {
                    name.set("MIT License")
                    distribution.set("repo")
                    url.set("${projectConfiguration.projectWeb}/blob/master/LICENSE.md")
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
                url.set(projectConfiguration.projectWeb)
            }
        }
        if (canSignPublication()) {
            signAllPublications()
        }
    }
}
