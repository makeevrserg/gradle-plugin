package ru.astrainteractive.gradleplugin.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign

class SigningPublicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            val signTasks = target.tasks.withType<Sign>()
            target.tasks
                .withType<AbstractPublishToMaven>()
                .forEach { publishTask ->
                    signTasks.forEach(publishTask::mustRunAfter)
                }
        }
    }
}
