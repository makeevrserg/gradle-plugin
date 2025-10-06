plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
    implementation(projects.buildLogic.plugins.convention)
}

gradlePlugin {
    website.set(projectWeb)
    vcsUrl.set(projectWeb)
    description = projectDescription
    plugins {
        create("minecraft.resource-processor") {
            id = "$projectGroup.$name"
            implementationClass = "$projectGroup.processor.plugin.ResourceProcessorPlugin"
            displayName = "KLibs minecraft resource processor plugin"
            description = "Minecraft resource processor plugin"
            tags.set(listOf("klibs"))
        }
    }
}
