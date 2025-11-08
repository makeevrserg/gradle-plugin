import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import java.nio.file.Files
import java.util.Properties
import kotlin.io.path.reader

plugins {
    `kotlin-dsl`
    alias(libs.plugins.vaniktech) apply false
//    id("ru.astrainteractive.gradleplugin.detekt") version "1.11.4" apply true
}

val klibs = libs

private fun requireFileProperty(key: String): Result<String> {
    return runCatching {
        val reader = project.file("gradle.properties").let { file ->
            val path = file.toPath()
            if (Files.isSymbolicLink(path)) {
                Files.readSymbolicLink(path).reader()
            } else {
                file.reader()
            }
        }
        val properties = Properties().apply { load(reader) }
        properties[key]?.toString() ?: error("Could not find property $key")
    }
}

private fun requireGradleProperty(key: String): Result<String> {
    return runCatching {
        rootProject.property(key)
            ?.toString()
            ?: error("Could not find property $key")
    }
}

private fun requireProperty(key: String): String {
    return requireGradleProperty(key)
        .getOrNull()
        ?: requireFileProperty(key).getOrThrow()
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
    val moduleName = project.name
    val whitelist = listOf(
        "android",
        "convention",
        "minecraft",
    )
    if (!whitelist.contains(moduleName)) return@subprojects
    project.apply(plugin = "java-gradle-plugin")
    project.apply(plugin = "com.vanniktech.maven.publish")

    afterEvaluate {
        configure<KotlinTopLevelExtension> {
            jvmToolchain(11)
        }
    }

    project.group = projectConfiguration.projectGroup
    project.version = projectConfiguration.projectVersionString
    project.description = projectConfiguration.projectDescription

    project.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_11
    }

    project.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
        publishToMavenCentral(automaticRelease = false)
        coordinates(
            groupId = projectConfiguration.projectGroup,
            artifactId = moduleName,
            version = projectConfiguration.projectVersionString
        )
        pom {
            name.set(projectConfiguration.projectName)
            description.set(projectConfiguration.projectDescription)
            url.set(projectConfiguration.projectWeb)

            licenses {
                license {
                    name.set("Apache-2.0")
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
        signAllPublications()
    }
}
