package ru.astrainteractive.gradleplugin.util

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.models.AndroidSdkInfo
import ru.astrainteractive.gradleplugin.models.JInfo
import ru.astrainteractive.gradleplugin.models.ProjectInfo
import ru.astrainteractive.gradleplugin.models.PublishInfo
import ru.astrainteractive.gradleplugin.util.GradleProperty.Companion.gradleProperty
import ru.astrainteractive.gradleplugin.util.SecretProperty.Companion.secretProperty

object ProjectProperties {
    val Project.androidSdkInfo: AndroidSdkInfo
        get() = AndroidSdkInfo(
            compile = gradleProperty("android.sdk.compile").integer,
            min = gradleProperty("android.sdk.min").integer,
            target = gradleProperty("android.sdk.target").integer
        )
    val Project.jinfo: JInfo
        get() = JInfo(
            ktarget = gradleProperty("java.ktarget").javaVersion,
            jtarget = gradleProperty("java.target").javaVersion,
            jsource = gradleProperty("java.source").javaVersion
        )

    val Project.projectInfo: ProjectInfo
        get() = ProjectInfo(
            name = gradleProperty("project.name").string,
            group = gradleProperty("project.group").string,
            versionString = gradleProperty("project.version.string").string,
            description = gradleProperty("project.description").string,
            url = gradleProperty("project.url").string,
            developersList = gradleProperty("publish.developers").developers,
        )

    val Project.publishInfo: PublishInfo
        get() = PublishInfo(
            libraryName = gradleProperty("publish.name").string,
            description = gradleProperty("publish.description").string,
            gitHubOrganization = gradleProperty("publish.repo.org").string,
            gitHubName = gradleProperty("publish.repo.name").string,
            license = gradleProperty("publish.license").string,
            publishGroupId = gradleProperty("publish.groupId").string,
            ossrhUsername = secretProperty("OSSRH_USERNAME").string,
            ossrhPassword = secretProperty("OSSRH_PASSWORD").string,
            signingKeyId = secretProperty("SIGNING_KEY_ID").string,
            signingPassword = secretProperty("SIGNING_PASSWORD").string,
            signingKey = secretProperty("SIGNING_KEY").string
        )
}
