package com.makeevrserg.gradleplugin.util

import java.io.InputStream
import java.util.Properties
import org.gradle.api.GradleException
import org.gradle.api.Project

class SecretProperty(path: String, private val project: Project) : BaseProperty(path) {
    @Suppress("MemberNameEqualsClassName")
    override val anyProperty: Any
        get() {
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

    companion object {
        fun Project.secretProperty(path: String) = GradleProperty(path, this)
    }
}