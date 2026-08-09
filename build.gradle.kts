/**
 * The published artifacts of this repository live in the `build-logic` included build.
 *
 * The root build itself has no sources; it only forwards the lifecycle tasks used by CI
 * (see `.github/workflows`) so that `./gradlew <task>` works from the repository root.
 */
val buildLogic = gradle.includedBuild("build-logic")

/**
 * Registers [taskName] on the root project, delegating to the task with the same name
 * on the root project of the `build-logic` included build.
 */
fun registerDelegatingTask(taskName: String, taskGroup: String, taskDescription: String) {
    tasks.register(taskName) {
        group = taskGroup
        description = taskDescription
        dependsOn(buildLogic.task(":$taskName"))
    }
}

registerDelegatingTask(
    taskName = "detekt",
    taskGroup = LifecycleBasePlugin.VERIFICATION_GROUP,
    taskDescription = "Runs detekt static analysis over build-logic"
)
registerDelegatingTask(
    taskName = "detektFormat",
    taskGroup = LifecycleBasePlugin.VERIFICATION_GROUP,
    taskDescription = "Runs detekt with auto correction over build-logic"
)
registerDelegatingTask(
    taskName = "test",
    taskGroup = LifecycleBasePlugin.VERIFICATION_GROUP,
    taskDescription = "Runs the build-logic tests"
)
registerDelegatingTask(
    taskName = "publishToMavenLocal",
    taskGroup = PublishingPlugin.PUBLISH_TASK_GROUP,
    taskDescription = "Publishes every build-logic module to the local Maven repository"
)
registerDelegatingTask(
    taskName = "publishToMavenCentral",
    taskGroup = PublishingPlugin.PUBLISH_TASK_GROUP,
    taskDescription = "Publishes every build-logic module to Maven Central"
)
