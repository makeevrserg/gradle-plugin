package ru.astrainteractive.gradleplugin.multiplatform.sourceset

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

internal class JvmSourceSet(
    private val project: Project,
    private val sourceSetName: String,
) {
    private val sourceSetMainFullName = "${sourceSetName}Main"
    private val sourceSetTestFullName = "${sourceSetName}Test"
    private fun configureSourceSetContainer() {
        project.configure<SourceSetContainer> {
            val main = getByName("main")
            val sourceSetMain = create(sourceSetMainFullName) {
                compileClasspath += main.compileClasspath + main.output
                runtimeClasspath += main.runtimeClasspath + main.output
            }
            val sourceSetTest = create(sourceSetTestFullName) {
                compileClasspath += sourceSetMain.compileClasspath + sourceSetMain.output
                runtimeClasspath += sourceSetMain.runtimeClasspath + sourceSetMain.output
            }
            project.tasks.create("${sourceSetName}Test", Test::class.java) {
                testClassesDirs += sourceSetTest.output.classesDirs
                classpath += sourceSetTest.runtimeClasspath
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

    fun configure() {
        configureSourceSetContainer()
        configureCompile()
        configurePublishing()
    }
}
