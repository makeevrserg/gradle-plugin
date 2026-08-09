plugins {
    `kotlin-dsl`
}

group = "ru.astrainteractive.gradleplugin.internal"

dependencies {
    implementation(libs.vaniktech)
    implementation(embeddedKotlin("gradle-plugin"))
}

gradlePlugin {
    plugins {
        create("projectConfiguration") {
            id = "klibs.project-configuration"
            implementationClass = "ru.astrainteractive.gradleplugin.internal.ProjectConfigurationPlugin"
        }
    }
}
