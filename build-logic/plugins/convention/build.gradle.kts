import com.gradle.publish.PluginBundleExtension

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
}

gradlePlugin {
    website.set(libs.versions.project.web.get())
    vcsUrl.set(libs.versions.project.web.get())
    description = libs.versions.project.description.get()
    isAutomatedPublishing = false
    plugins {
        create("detekt") {
            id = "${libs.versions.project.group.get()}.detekt"
            implementationClass = "${libs.versions.project.group.get()}.CoreDetektPlugin"
            displayName = name
        }
        create("dokka-module") {
            id = "${libs.versions.project.group.get()}.dokka.module"
            implementationClass = "${libs.versions.project.group.get()}.DokkaModulePlugin"
            displayName = name
        }
        create("dokka-root") {
            id = "${libs.versions.project.group.get()}.dokka.root"
            implementationClass = "${libs.versions.project.group.get()}.DokkaRootPlugin"
            displayName = name
        }
        create("java-core") {
            id = "${libs.versions.project.group.get()}.java.core"
            implementationClass = "${libs.versions.project.group.get()}.JvmSourceTargetPlugin"
            displayName = name
        }
        create("root-info") {
            id = "${libs.versions.project.group.get()}.root.info"
            implementationClass = "${libs.versions.project.group.get()}.InfoRootPlugin"
            displayName = name
        }
        create("publication") {
            id = "${libs.versions.project.group.get()}.publication"
            implementationClass = "${libs.versions.project.group.get()}.PublicationPlugin"
            displayName = name
        }
    }
}



