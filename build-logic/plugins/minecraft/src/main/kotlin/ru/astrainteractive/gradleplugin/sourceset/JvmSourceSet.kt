package ru.astrainteractive.gradleplugin.sourceset

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

class JvmSourceSet(
    project: Project,
    sourceSetName: String,
) {
    private val sourceSetFullName = "${sourceSetName}Main"

    init {
        project.configure<SourceSetContainer> {
            val main = getByName("main")
            create(sourceSetFullName) {
                compileClasspath += main.compileClasspath
                runtimeClasspath += main.runtimeClasspath
                compileClasspath += main.output
                runtimeClasspath += main.output
            }
        }
        project.configurations.create("${sourceSetFullName}Compile") {
            extendsFrom(project.configurations["implementation"])
        }
        project.configure<PublishingExtension> {
            publications {
                val sourceJar = project.tasks.register("${sourceSetFullName}Jar", Jar::class.java) {
                    val sourceSets = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets
                    from(sourceSets[sourceSetFullName].output)
                }
                val sourceSourcesJar = project.tasks.register("${sourceSetFullName}SourcesJar", Jar::class.java) {
                    archiveClassifier.set("sources")
                    val sourceSets = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets
                    from(sourceSets[sourceSetFullName].allSource)
                }
                val sourceJavaDocJar = project.tasks.register("${sourceSetFullName}JavadocJar", Jar::class.java) {
                    archiveClassifier.set("javadoc")
                }
                register("maven$sourceSetFullName", MavenPublication::class) {
                    artifactId = "${project.name}-$sourceSetName"
                    artifact(sourceJar.get())
                    artifact(sourceSourcesJar.get())
                    artifact(sourceJavaDocJar.get())
                }
            }
        }
    }

    companion object {
        fun Project.configureAstraSourceSet(name: String) {
            JvmSourceSet(this, name)
        }

        fun Project.configureDefaultAstraHierarchy() {
            configureAstraSourceSet("bukkit")
            configureAstraSourceSet("velocity")
            configureAstraSourceSet("fabric")
            configureAstraSourceSet("forge")
        }
    }
}
