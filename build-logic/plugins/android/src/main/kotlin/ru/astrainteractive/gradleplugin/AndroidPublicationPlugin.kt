package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure

class AndroidPublicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.plugins) {
            apply(PublicationPlugin::class.java)
        }

        target.afterEvaluate {
            target.configure<PublishingExtension> {
                publications.create("release", MavenPublication::class.java) {
                    from(target.components.getByName("release"))
                }
            }
        }
    }
}
