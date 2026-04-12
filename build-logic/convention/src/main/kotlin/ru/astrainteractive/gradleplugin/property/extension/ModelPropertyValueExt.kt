package ru.astrainteractive.gradleplugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradle.property.api.PropertyValue
import ru.astrainteractive.gradle.property.api.klibsGradleProperty
import ru.astrainteractive.gradleplugin.property.extension.ExtendedPropertyValueExt.requireJavaVersion
import ru.astrainteractive.gradleplugin.property.extension.ExtendedPropertyValueExt.requireJvmTarget
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.requireString
import ru.astrainteractive.gradleplugin.property.mapping.DeveloperMapper
import ru.astrainteractive.gradleplugin.property.model.Developer
import ru.astrainteractive.gradleplugin.property.model.JInfo
import ru.astrainteractive.gradleplugin.property.model.ProjectInfo
import ru.astrainteractive.gradleplugin.property.model.PublishInfo

object ModelPropertyValueExt {
    val Project.requireJinfo: JInfo
        get() = JInfo(
            ktarget = klibsGradleProperty("java.ktarget").requireJvmTarget,
            jtarget = klibsGradleProperty("java.target").requireJavaVersion,
            jsource = klibsGradleProperty("java.source").requireJavaVersion
        )

    val Project.requireProjectInfo: ProjectInfo
        get() = ProjectInfo(
            name = klibsGradleProperty("project.name").requireString,
            group = klibsGradleProperty("project.group").requireString,
            versionString = klibsGradleProperty("project.version.string").requireString,
            description = klibsGradleProperty("project.description").requireString,
            url = klibsGradleProperty("project.url").requireString,
            developersList = klibsGradleProperty("project.developers").requireDevelopers,
        )

    val Project.requirePublishInfo: PublishInfo
        get() = PublishInfo(
            libraryName = klibsGradleProperty("publish.name").requireString,
            description = klibsGradleProperty("publish.description").requireString,
            gitHubOrganization = klibsGradleProperty("publish.repo.org").requireString,
            gitHubName = klibsGradleProperty("publish.repo.name").requireString,
            license = klibsGradleProperty("publish.license").requireString,
            publishGroupId = klibsGradleProperty("publish.groupId").requireString,
        )

    // Developers
    val PropertyValue.developers: Result<List<Developer>>
        get() = getValue().mapCatching(DeveloperMapper::parseDevelopers)

    val PropertyValue.requireDevelopers: List<Developer>
        get() = developers.getOrThrow()

    /**
     * This value will automatically create group based on folders names
     *
     * e.x :components:core:resource -> (com.example).components.core.resource
     */
    val Project.hierarchyGroup: String
        get() = "${requireProjectInfo.group}.$path"
            .replace("-", ".")
            .replace(":", ".")
            .replace("..", ".")
            .lowercase()
}
