plugins {
    `kotlin-dsl`
    id("org.gradle.maven-publish")
    id("signing")
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
    plugins {
        create("detekt") {
            id = "${libs.versions.project.group.get()}.detekt"
            implementationClass = "${libs.versions.project.group.get()}.CoreDetektPlugin"
        }
        create("dokka-module") {
            id = "${libs.versions.project.group.get()}.dokka.module"
            implementationClass = "${libs.versions.project.group.get()}.DokkaModulePlugin"
        }
        create("dokka-root") {
            id = "${libs.versions.project.group.get()}.dokka.root"
            implementationClass = "${libs.versions.project.group.get()}.DokkaRootPlugin"
        }
        create("java-core") {
            id = "${libs.versions.project.group.get()}.java.core"
            implementationClass = "${libs.versions.project.group.get()}.JvmSourceTargetPlugin"
        }
        create("root-info") {
            id = "${libs.versions.project.group.get()}.root.info"
            implementationClass = "${libs.versions.project.group.get()}.InfoRootPlugin"
        }
        create("publication") {
            id = "${libs.versions.project.group.get()}.publication"
            implementationClass = "${libs.versions.project.group.get()}.PublicationPlugin"
        }
    }
}
