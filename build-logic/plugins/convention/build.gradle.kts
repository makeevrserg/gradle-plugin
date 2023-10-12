plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
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
    plugins {
        create("detekt") {
            id = "${libs.versions.project.group.get()}.detekt"
            implementationClass = "${libs.versions.project.group.get()}.detekt.DefaultDetektPlugin"
            displayName = "KLibs detekt plugin"
            description = "Default setup for detekt plugin"
            tags.set(listOf("klibs"))
        }
        create("detekt.compose") {
            id = "${libs.versions.project.group.get()}.detekt.compose"
            implementationClass = "${libs.versions.project.group.get()}.detekt.ComposeDetektPlugin"
            displayName = "KLibs compose-detekt plugin"
            description = "Setup for detekt compose integration"
            tags.set(listOf("klibs"))
        }
        create("dokka.module") {
            id = "${libs.versions.project.group.get()}.dokka.module"
            implementationClass = "${libs.versions.project.group.get()}.DokkaModulePlugin"
            displayName = "KLibs dokka for module configuration"
            description = "Dokka generation for project module"
            tags.set(listOf("klibs"))
        }
        create("dokka.root") {
            id = "${libs.versions.project.group.get()}.dokka.root"
            implementationClass = "${libs.versions.project.group.get()}.DokkaRootPlugin"
            displayName = "KLibs Dokka for root project"
            description = "Dokka generator for root project"
            tags.set(listOf("klibs"))
        }
        create("java.core") {
            id = "${libs.versions.project.group.get()}.java.core"
            implementationClass = "${libs.versions.project.group.get()}.JvmSourceTargetPlugin"
            displayName = "KLibs java configuration"
            description = "Default java configuration"
            tags.set(listOf("klibs"))
        }
        create("stub.javadoc") {
            id = "${libs.versions.project.group.get()}.stub.javadoc"
            implementationClass = "${libs.versions.project.group.get()}.StubJavaDocPlugin"
            displayName = "KLibs stub javadoc plugin"
            description = "Generates stub javadoc"
            tags.set(listOf("klibs"))
        }
        create("root.info") {
            id = "${libs.versions.project.group.get()}.root.info"
            implementationClass = "${libs.versions.project.group.get()}.InfoRootPlugin"
            displayName = "KLibs root info plugin"
            description = "Generates version, description for module"
            tags.set(listOf("klibs"))
        }
        create("publication") {
            id = "${libs.versions.project.group.get()}.publication"
            implementationClass = "${libs.versions.project.group.get()}.PublicationPlugin"
            displayName = "KLibs publication plugin"
            description = "Default pulbication plugin"
            tags.set(listOf("klibs"))
        }
    }
}
