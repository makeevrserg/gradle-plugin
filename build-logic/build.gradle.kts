import com.vanniktech.maven.publish.SonatypeHost

plugins {
    `kotlin-dsl`
    id("com.vanniktech.maven.publish") version "0.30.0" apply false
    id("ru.astrainteractive.gradleplugin.detekt") version "1.4.0" apply true
    alias(libs.plugins.gradle.shadow) apply false
}

val klibs = libs

subprojects {
    val project = this
    val moduleName = project.name
    val whitelist = listOf(
        "android",
        "convention",
        "minecraft",
    )
    if (!whitelist.contains(moduleName)) return@subprojects
    project.apply(plugin = "java-gradle-plugin")
    project.apply(plugin = "com.vanniktech.maven.publish")

    project.group = klibs.versions.project.group.get()
    project.version = klibs.versions.project.version.string.get()
    project.description = klibs.versions.project.description.get()

    project.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_11
    }

    project.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
        publishToMavenCentral(
            host = SonatypeHost.CENTRAL_PORTAL,
            automaticRelease = false
        )

        signAllPublications()
        coordinates(
            groupId = klibs.versions.project.group.get(),
            artifactId = moduleName,
            version = klibs.versions.project.version.string.get()
        )
        pom {
            name.set(klibs.versions.project.name.get())
            description.set(klibs.versions.project.description.get())
            url.set(klibs.versions.project.web.get())

            licenses {
                license {
                    name.set("Apache-2.0")
                    distribution.set("repo")
                    url.set("${klibs.versions.project.web.get()}/blob/master/LICENSE.md")
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
                url.set(klibs.versions.project.web.get())
            }
        }
    }
}
