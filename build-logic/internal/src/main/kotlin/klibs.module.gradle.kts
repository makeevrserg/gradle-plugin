import com.vanniktech.maven.publish.DeploymentValidation
import ru.astrainteractive.gradleplugin.internal.ProjectConfiguration
import ru.astrainteractive.gradleplugin.internal.ProjectConfigurationPlugin

plugins {
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

apply<ProjectConfigurationPlugin>()

val klibs = extensions.getByType<ProjectConfiguration>()

group = klibs.group
version = klibs.versionString
description = klibs.description

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_17
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val canSignPublication: Boolean = providers.gradleProperty("mavenCentralUsername").isPresent
    .or(providers.environmentVariable("ORG_GRADLE_PROJECT_mavenCentralUsername").isPresent)

mavenPublishing {
    publishToMavenCentral(
        automaticRelease = true,
        validateDeployment = DeploymentValidation.VALIDATED
    )
    coordinates(
        groupId = klibs.group,
        artifactId = project.name,
        version = klibs.versionString
    )
    pom {
        name.set(klibs.name)
        description.set(klibs.description)
        url.set(klibs.url)

        licenses {
            license {
                name.set("MIT License")
                distribution.set("repo")
                url.set("${klibs.url}/blob/master/LICENSE.md")
            }
        }
        developers {
            developer {
                id.set("makeevrserg")
                name.set("Roman Makeev")
                email.set("makeevrserg@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:ssh://github.com/makeevrserg/gradle-plugin.git")
            developerConnection.set("scm:git:ssh://github.com/makeevrserg/gradle-plugin.git")
            url.set(klibs.url)
        }
    }
    if (canSignPublication) {
        signAllPublications()
    }
}
