plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(libs.gradle.shadow)
    implementation(projects.buildLogic.plugins.convention)
}

gradlePlugin {
    website.set(libs.versions.project.web.get())
    vcsUrl.set(libs.versions.project.web.get())
    description = libs.versions.project.description.get()
    isAutomatedPublishing = false
    plugins {
        create("empty-minecraft") {
            id = "${libs.versions.project.group.get()}.minecraft.empty"
            implementationClass = "${libs.versions.project.group.get()}.EmptyMinecraftPlugin"
            displayName = name
        }
        create("minecraft-multiplatform") {
            id = "${libs.versions.project.group.get()}.minecraft.multiplatform"
            implementationClass = "${libs.versions.project.group.get()}.multiplatform.MinecraftMultiplatformPlugin"
            displayName = name
        }
    }
}
