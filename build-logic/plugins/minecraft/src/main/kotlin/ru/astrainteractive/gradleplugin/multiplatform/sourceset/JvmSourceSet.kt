package ru.astrainteractive.gradleplugin.multiplatform.sourceset

import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin.VERIFICATION_GROUP
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal class JvmSourceSet(
    private val project: Project,
    private val sourceSetName: String,
) {
    private val sourceSetMainFullName = "${sourceSetName}Main"
    private val sourceSetTestFullName = "${sourceSetName}Test"

    private fun SourceSetContainer.configureMainSourceSet() {
        val main = getByName("main")
        create(sourceSetMainFullName) {
            compileClasspath += main.compileClasspath + main.output
            runtimeClasspath += main.runtimeClasspath + main.output
        }
    }

    private fun SourceSetContainer.configureTestSourceSet() {
        val mainSourceSet = getByName("main")
        val testSourceSet = getByName("test")
        val customSourceSetTest = create(sourceSetTestFullName) {
            compileClasspath += mainSourceSet.compileClasspath + mainSourceSet.output
            runtimeClasspath += mainSourceSet.runtimeClasspath + mainSourceSet.output

            compileClasspath += testSourceSet.compileClasspath + testSourceSet.output
            runtimeClasspath += testSourceSet.runtimeClasspath + testSourceSet.output
        }

        project.tasks.create(sourceSetTestFullName, Test::class.java) {
            testClassesDirs += customSourceSetTest.output.classesDirs
            classpath += customSourceSetTest.runtimeClasspath
            group = VERIFICATION_GROUP
        }
    }

    private fun configureKotlinInternals() {
        val compilations = project
            .extensions
            .getByType(KotlinJvmProjectExtension::class.java)
            .target
            .compilations

        compilations.getByName(sourceSetMainFullName)
            .associateWith(compilations.getByName("main"))

        compilations.getByName(sourceSetTestFullName).apply {
            associateWith(compilations.getByName(sourceSetMainFullName))
            associateWith(compilations.getByName("test"))
            associateWith(compilations.getByName("main"))
        }
    }

    private fun configureSourceSetContainer() {
        project.configure<SourceSetContainer> {
            configureMainSourceSet()
            configureTestSourceSet()
        }
    }

    private fun configureCompile() {
        listOf(sourceSetMainFullName, sourceSetTestFullName).forEach { sourceSetName ->
            project.configurations
                .filter { configuration -> configuration.name.contains(sourceSetName) }
                .forEach { configuration ->
                    println("NAME: ${configuration.name}")
                    val type = configuration.name
                        .replaceFirst(sourceSetName, "")
                        .replaceFirstChar { ch -> ch.lowercase() }
                    configuration.extendsFrom(project.configurations["$type"])
                }
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
        configureKotlinInternals()
        configurePublishing()
    }
}
