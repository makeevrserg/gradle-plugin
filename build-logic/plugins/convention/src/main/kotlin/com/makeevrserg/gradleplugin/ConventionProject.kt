package com.makeevrserg.gradleplugin

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.InputStream
import java.util.Properties

/**
 * This file contains custom secret and other required fields
 */
object ConventionProject {
    val JAVA_VERSION = JavaVersion.VERSION_11
    val Project.KEY_ALIAS: String
        get() = getCredential(this, "KEY_ALIAS")
    val Project.KEY_PASSWORD: String
        get() = getCredential(this, "KEY_PASSWORD")
    val Project.STORE_PASSWORD: String
        get() = getCredential(this, "STORE_PASSWORD")

    private fun getCredential(project: Project, path: String): String {
        val properties: Properties = Properties()
        val localProperties = project.rootProject.file("local.properties")
        if (!localProperties.exists()) return "NO_LOCAL_PROPERTIES"
        val inputStream: InputStream = localProperties.inputStream()
        properties.load(inputStream)
        return properties.getProperty(path)
    }
}
