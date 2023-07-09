package ru.astrainteractive.gradleplugin.util

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.Properties

class SecretProperty(path: String, private val project: Project) : BaseProperty(path) {
    @Suppress("MemberNameEqualsClassName")
    override val anyProperty: Any
        get() {
            // try to get system ci property
            val systemEnvProperty = System.getenv(property)
            if (systemEnvProperty == null) {
                println("System.enviroment ${property} is missing. Getting it from local.properties")
            } else return systemEnvProperty
            // if not ci getting from local.properties
            val properties = Properties().apply {
                val secretPropsFile = project.rootProject.file("local.properties")
                if (!secretPropsFile.exists()) throw GradleException("No local.properties file found")
                load(secretPropsFile.reader())
            }
            return properties.getProperty(property) ?: throw GradleException("Required property $property not defined!")
        }

    companion object {
        fun Project.secretProperty(path: String) = SecretProperty(path, this)
    }
}
