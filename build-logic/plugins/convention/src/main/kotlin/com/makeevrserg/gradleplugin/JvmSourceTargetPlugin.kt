package com.makeevrserg.gradleplugin

import com.makeevrserg.gradleplugin.util.GradleProperty.Companion.gradleProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class JvmSourceTargetPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configure<JavaPluginExtension> {
            withSourcesJar()
            withJavadocJar()
            sourceCompatibility = target.gradleProperty("java.source").javaVersion
            targetCompatibility = target.gradleProperty("java.target").javaVersion
        }
        target.tasks.withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        target.tasks
            .withType<KotlinCompile>()
            .configureEach {
                kotlinOptions.jvmTarget = target.gradleProperty("java.ktarget").javaVersion.majorVersion
            }
        target.plugins.withId("org.gradle.maven-publish") {
            target.configure<PublishingExtension> {
                publications.register("mavenJava", MavenPublication::class) {
                    from(target.components["java"])
                }
            }
        }
    }
}
