plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.dokka.gradle)
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
