package ru.astrainteractive.gradleplugin.plugin

import com.varabyte.kobweb.gradle.application.tasks.KobwebCacheAppFrontendDataTask
import com.varabyte.kobweb.gradle.application.tasks.KobwebCopySupplementalResourcesTask
import com.varabyte.kobweb.gradle.application.tasks.KobwebCopyWorkerJsOutputTask
import com.varabyte.kobweb.gradle.application.tasks.KobwebGenerateSiteEntryTask
import com.varabyte.kobweb.gradle.application.tasks.KobwebGenerateSiteIndexTask
import com.varabyte.kobweb.gradle.application.tasks.KobwebStartTask
import com.varabyte.kobweb.gradle.application.tasks.KobwebUnpackServerJarTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

abstract class KobwebResourcesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val scope = target.extensions.create(
            name = "jsResources",
            type = JsResourcesScope::class,
        )

        val copySharedResources = target.tasks.register<Copy>("copyJsResources") {
            from(
                scope.projectsPaths
                    .map { projectPath ->
                        project.project(projectPath)
                            .file("src/jsMain/resources/public")
                    }
            )
            into(project.layout.buildDirectory.dir("processedResources/js/main/public"))
        }

        target.tasks
            .matching { task ->
                when (task) {
                    is KobwebStartTask,
                    is KobwebUnpackServerJarTask,
                    is Sync,
                    is KobwebGenerateSiteEntryTask,
                    is KobwebCacheAppFrontendDataTask,
                    is KobwebGenerateSiteIndexTask,
                    is KobwebCopyWorkerJsOutputTask,
                    is KobwebCopySupplementalResourcesTask -> true

                    else -> false
                }
            }
            .configureEach { dependsOn(copySharedResources) }
    }

    open class JsResourcesScope {
        var projectsPaths: List<String> = emptyList()
    }
}
