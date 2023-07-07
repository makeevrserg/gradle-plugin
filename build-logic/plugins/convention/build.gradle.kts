import java.io.InputStream
import java.util.Base64
import java.util.Properties

plugins {
    `kotlin-dsl`
    id("org.gradle.maven-publish")
    id("signing")
    id("java-gradle-plugin")
}

group = libs.versions.project.group.get()
version = libs.versions.project.version.string.get()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.toolsBuild)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.android)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
}
gradlePlugin {
    plugins {
        create("android-core") {
            id = "${libs.versions.project.group.get()}.android.core"
            implementationClass = "${libs.versions.project.group.get()}.AndroidSdkPlugin"
        }
        create("android-apk-name") {
            id = "${libs.versions.project.group.get()}.android.apk.name"
            implementationClass = "${libs.versions.project.group.get()}.ApkNamePlugin"
        }
        create("android-publication") {
            id = "${libs.versions.project.group.get()}.android.publication"
            implementationClass = "${libs.versions.project.group.get()}.AndroidPublicationPlugin"
        }
        create("detekt") {
            id = "${libs.versions.project.group.get()}.detekt"
            implementationClass = "${libs.versions.project.group.get()}.DetektPlugin"
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

fun getSecretProperty(property: String): String {
    val property = "makeevrserg.$property"
    // try to get system ci property
    System.getenv(property)?.let { return it }
    // if not ci getting from local.properties
    val properties = Properties().apply {
        val localProperties = project.rootProject.file("local.properties")
        if (!localProperties.exists()) throw GradleException("No local.properties file found")
        val inputStream: InputStream = localProperties.inputStream()
        load(inputStream)
    }
    return properties.getProperty(property) ?: throw GradleException("Required property $property not defined!")
}

publishing {

    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"

        credentials {
            username = getSecretProperty("OSSRH_USERNAME")
            password = getSecretProperty("OSSRH_PASSWORD")
        }
    }
    publications.register("mavenJava", MavenPublication::class) {
        from(components["java"])

        pom {
            name.set("GradlePlugin")
            description.set("Custom gradle plugin for my libraries and projects")
            url.set("https://github.com/makeevrserg/gradle-plugin")
            licenses {
                license {
                    name.set("Apache-2.0")
                    distribution.set("repo")
                    url.set("https://github.com/makeevrserg/gradle-plugin/blob/master/LICENSE.md")
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
                url.set("https://github.com/makeevrserg/gradle-plugin")
            }
        }
    }

}



signing {
    val SIGNING_KEY = getSecretProperty("SIGNING_KEY")
    val SIGNING_KEY_ID = getSecretProperty("SIGNING_KEY_ID")
    val SIGNING_PASSWORD = getSecretProperty("SIGNING_PASSWORD")
    useInMemoryPgpKeys(SIGNING_KEY_ID, SIGNING_KEY, SIGNING_PASSWORD)
    sign(publishing.publications)
}
