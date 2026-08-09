plugins {
    `kotlin-dsl`
    id("ru.astrainteractive.gradleplugin.detekt") version "2.3.0" apply true
}

private fun registerAggregateTask(taskName: String, taskGroup: String, taskDescription: String) {
    tasks.register(taskName) {
        group = taskGroup
        description = taskDescription
        dependsOn(subprojects.map { subproject -> "${subproject.path}:$taskName" })
    }
}

registerAggregateTask(
    taskName = "publishToMavenLocal",
    taskGroup = PublishingPlugin.PUBLISH_TASK_GROUP,
    taskDescription = "Publishes every build-logic module to the local Maven repository"
)
registerAggregateTask(
    taskName = "publishAndReleaseToMavenCentral",
    taskGroup = PublishingPlugin.PUBLISH_TASK_GROUP,
    taskDescription = "Publishes every build-logic module to Maven Central"
)

tasks.named("test") {
    dependsOn(subprojects.map { subproject -> "${subproject.path}:test" })
}
