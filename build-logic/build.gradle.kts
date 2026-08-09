import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

plugins {
    `kotlin-dsl`
    alias(libs.plugins.vaniktech) apply false
    id("ru.astrainteractive.gradleplugin.detekt") version "2.3.0" apply true
}

private fun requireProperty(key: String): String {
    return rootProject.findProperty(key)
        ?.toString()
        ?: throw GradleException("Could not find property $key in gradle.properties")
}

private fun canSignPublication(): Boolean {
    val hasEnvMavenUsername = System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") != null
    val hasGradlePropertyUsername = providers.gradleProperty("mavenCentralUsername").isPresent
    return hasEnvMavenUsername || hasGradlePropertyUsername
}

data class ProjectConfiguration(
    val projectName: String = requireProperty("klibs.project.name"),
    val projectDescription: String = requireProperty("klibs.project.description"),
    val projectGroup: String = requireProperty("klibs.project.group"),
    val projectUrl: String = requireProperty("klibs.project.url"),
    val projectVersionString: String = requireProperty("klibs.project.version.string")
)

val projectConfiguration = ProjectConfiguration()

/**
 * Lifecycle task on the `build-logic` root project which runs [taskName] in every subproject.
 *
 * Subprojects are resolved by path so that adding a new module does not require touching this file
 * or the CI workflows.
 */
private fun registerAggregateTask(taskName: String, taskGroup: String, taskDescription: String) {
    tasks.register(taskName) {
        group = taskGroup
        description = taskDescription
        dependsOn(subprojects.map { subproject -> "${subproject.path}:$taskName" })
    }
}

registerAggregateTask(
    taskName = "publishToMavenLocal",
    taskGroup = PublishingPlugin.PUBLISH_TASK_GROUP,
    taskDescription = "Publishes every build-logic module to the local Maven repository"
)
registerAggregateTask(
    taskName = "publishToMavenCentral",
    taskGroup = PublishingPlugin.PUBLISH_TASK_GROUP,
    taskDescription = "Publishes every build-logic module to Maven Central"
)

tasks.named("test") {
    dependsOn(subprojects.map { subproject -> "${subproject.path}:test" })
}

allprojects {
    extensions.add("projectName", projectConfiguration.projectName)
    extensions.add("projectDescription", projectConfiguration.projectDescription)
    extensions.add("projectGroup", projectConfiguration.projectGroup)
    extensions.add("projectUrl", projectConfiguration.projectUrl)
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
            url.set(projectConfiguration.projectUrl)

            licenses {
                license {
                    name.set("MIT License")
                    distribution.set("repo")
                    url.set("${projectConfiguration.projectUrl}/blob/master/LICENSE.md")
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
                url.set(projectConfiguration.projectUrl)
            }
        }
        if (canSignPublication()) {
            signAllPublications()
        }
    }
}
