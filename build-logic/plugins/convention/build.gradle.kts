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

fun requireProperty(name: String): String {
    return extra.get(name)
        ?.toString()
        ?: throw GradleException("Not found $name extension")
}

gradlePlugin {
    website.set(requireProperty("project.web"))
    vcsUrl.set(requireProperty("project.web"))
    description = requireProperty("project.description")
    val group = requireProperty("project.group")
    plugins {
        create("detekt") {
            id = "$group.$name"
            implementationClass = "$group.detekt.DefaultDetektPlugin"
            displayName = "KLibs detekt plugin"
            description = "Default setup for detekt plugin"
            tags.set(listOf("klibs"))
        }
        create("detekt.compose") {
            id = "$group.$name"
            implementationClass = "$group.detekt.ComposeDetektPlugin"
            displayName = "KLibs compose-detekt plugin"
            description = "Setup for detekt compose integration"
            tags.set(listOf("klibs"))
        }
        create("dokka.module") {
            id = "$group.$name"
            implementationClass = "$group.DokkaModulePlugin"
            displayName = "KLibs dokka for module configuration"
            description = "Dokka generation for project module"
            tags.set(listOf("klibs"))
        }
        create("dokka.root") {
            id = "$group.$name"
            implementationClass = "$group.DokkaRootPlugin"
            displayName = "KLibs Dokka for root project"
            description = "Dokka generator for root project"
            tags.set(listOf("klibs"))
        }
        create("java.core") {
            id = "$group.$name"
            implementationClass = "$group.JvmSourceTargetPlugin"
            displayName = "KLibs java configuration"
            description = "Default java configuration"
            tags.set(listOf("klibs"))
        }
        create("stub.javadoc") {
            id = "$group.$name"
            implementationClass = "$group.StubJavaDocPlugin"
            displayName = "KLibs stub javadoc plugin"
            description = "Generates stub javadoc"
            tags.set(listOf("klibs"))
        }
        create("root.info") {
            id = "$group.$name"
            implementationClass = "$group.InfoRootPlugin"
            displayName = "KLibs root info plugin"
            description = "Generates version, description for module"
            tags.set(listOf("klibs"))
        }
        create("publication") {
            id = "$group.$name"
            implementationClass = "$group.PublicationPlugin"
            displayName = "KLibs publication plugin"
            description = "Default pulbication plugin"
            tags.set(listOf("klibs"))
        }
        create("publication.kmp-signing") {
            id = "$group.$name"
            implementationClass = "$group.SigningPublicationPlugin"
            displayName = "KLibs publication signing plugin"
            description = "Default pulbication signing plugin"
            tags.set(listOf("klibs"))
        }
    }
}
