package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign

class SigningPublicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.afterEvaluate {
            target.tasks
                .withType<AbstractPublishToMaven>()
                .forEach { publishTask ->
                    target.tasks.withType<Sign>().forEach(publishTask::mustRunAfter)
                }
        }
    }
}
