package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

/**
 * @author https://github.com/icerockdev/moko-gradle-plugin
 */
class StubJavaDocPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.plugins) {
            apply("org.gradle.maven-publish")
        }

        val javadocJar = target.tasks.register("javadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
        }

        target.configure<PublishingExtension> {
            publications.withType<MavenPublication> {
                artifact(javadocJar.get())
            }
        }
    }
}
