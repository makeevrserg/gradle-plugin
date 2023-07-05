plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.gradle.dokka) apply true
    alias(libs.plugins.gradle.shadow) apply true
    id("dokka-root")
    id("com.makeevrserg.gradleplugin.detekt")
    id("com.makeevrserg.gradleplugin.root.info")
}

tasks.register("cleanProject", Delete::class) {
    fun clearProject(project: Project) {
        project.childProjects.values.forEach(::clearProject)
        delete(project.buildDir)
    }
    clearProject(rootProject)
}
