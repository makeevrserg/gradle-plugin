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
    private val project: Project,
    private val sourceSetName: String,
) {
    private val sourceSetMainFullName = "${sourceSetName}Main"
    private val sourceSetTestFullName = "${sourceSetName}Test"
    private fun configureSourceSetContainer() {
        project.configure<SourceSetContainer> {
            val main = getByName("main")
            val test = getByName("test")
            create(sourceSetMainFullName) {
                compileClasspath += main.compileClasspath + main.output
                runtimeClasspath += main.runtimeClasspath + main.output
            }
            create(sourceSetTestFullName) {
                compileClasspath += test.compileClasspath + test.output
                runtimeClasspath += test.runtimeClasspath + test.output
            }
        }
    }

    private fun configureCompile() {
        project.configurations.create("${sourceSetMainFullName}Compile") {
            extendsFrom(project.configurations["implementation"])
        }
        project.configurations.create("${sourceSetTestFullName}Compile") {
            extendsFrom(project.configurations["testImplementation"])
        }
    }

    private fun configurePublishing() {
        if (project.extensions.findByType(PublishingExtension::class.java) == null) return
        project.configure<PublishingExtension> {
            publications {
                val sourceJar = project.tasks.register("${sourceSetMainFullName}Jar", Jar::class.java) {
                    val sourceSets = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets
                    from(sourceSets[sourceSetMainFullName].output)
                }
                val sourceSourcesJar = project.tasks.register("${sourceSetMainFullName}SourcesJar", Jar::class.java) {
                    archiveClassifier.set("sources")
                    val sourceSets = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets
                    from(sourceSets[sourceSetMainFullName].allSource)
                }
                val sourceJavaDocJar = project.tasks.register("${sourceSetMainFullName}JavadocJar", Jar::class.java) {
                    archiveClassifier.set("javadoc")
                }

                register("maven$sourceSetMainFullName", MavenPublication::class) {
                    artifactId = "${project.name}-$sourceSetName"
                    artifact(sourceJar.get())
                    artifact(sourceSourcesJar.get())
                    artifact(sourceJavaDocJar.get())
                }
            }
        }
    }

    init {
        configureSourceSetContainer()
        configureCompile()
        configurePublishing()
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
