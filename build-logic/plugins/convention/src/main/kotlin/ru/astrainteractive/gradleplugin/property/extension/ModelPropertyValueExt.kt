package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.model.Developer
import ru.astrainteractive.gradleplugin.model.JInfo
import ru.astrainteractive.gradleplugin.model.ProjectInfo
import ru.astrainteractive.gradleplugin.model.PublishInfo
import ru.astrainteractive.gradleplugin.property.PropertyValue
import ru.astrainteractive.gradleplugin.property.PropertyValue.Companion.baseGradleProperty
import ru.astrainteractive.gradleplugin.property.PropertyValue.Companion.baseSecretProperty
import ru.astrainteractive.gradleplugin.property.extension.ExtendedPropertyValueExt.requireJavaVersion
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString
import ru.astrainteractive.gradleplugin.property.mapping.DeveloperMapper

object ModelPropertyValueExt {
    val Project.requireJinfo: JInfo
        get() = JInfo(
            ktarget = baseGradleProperty("java.ktarget").requireJavaVersion,
            jtarget = baseGradleProperty("java.target").requireJavaVersion,
            jsource = baseGradleProperty("java.source").requireJavaVersion
        )

    val Project.requireProjectInfo: ProjectInfo
        get() = ProjectInfo(
            name = baseGradleProperty("project.name").requireString,
            group = baseGradleProperty("project.group").requireString,
            versionString = baseGradleProperty("project.version.string").requireString,
            description = baseGradleProperty("project.description").requireString,
            url = baseGradleProperty("project.url").requireString,
            developersList = baseGradleProperty("project.developers").requireDevelopers,
        )

    val Project.requirePublishInfo: PublishInfo
        get() = PublishInfo(
            libraryName = baseGradleProperty("publish.name").requireString,
            description = baseGradleProperty("publish.description").requireString,
            gitHubOrganization = baseGradleProperty("publish.repo.org").requireString,
            gitHubName = baseGradleProperty("publish.repo.name").requireString,
            license = baseGradleProperty("publish.license").requireString,
            publishGroupId = baseGradleProperty("publish.groupId").requireString,
            ossrhUsername = baseSecretProperty("OSSRH_USERNAME").requireString,
            ossrhPassword = baseSecretProperty("OSSRH_PASSWORD").requireString,
            signingKeyId = baseSecretProperty("SIGNING_KEY_ID").requireString,
            signingPassword = baseSecretProperty("SIGNING_PASSWORD").requireString,
            signingKey = baseSecretProperty("SIGNING_KEY").requireString
        )

    // Developers
    val PropertyValue.developers: Result<List<Developer>>
        get() = value.mapCatching(DeveloperMapper::parseDevelopers)
    val PropertyValue.requireDevelopers: List<Developer>
        get() = developers.getOrThrow()
}
