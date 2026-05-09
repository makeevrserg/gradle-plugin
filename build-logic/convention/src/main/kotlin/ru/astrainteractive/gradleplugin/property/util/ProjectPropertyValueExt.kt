package ru.astrainteractive.gradleplugin.property.util

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.astrainteractive.gradle.property.api.PropertyValue
import ru.astrainteractive.gradle.property.api.klibsGradleProperty
import ru.astrainteractive.gradleplugin.property.mapping.DeveloperMapper
import ru.astrainteractive.gradleplugin.property.model.AndroidSdkInfo
import ru.astrainteractive.gradleplugin.property.model.Developer
import ru.astrainteractive.gradleplugin.property.model.JInfo
import ru.astrainteractive.gradleplugin.property.model.ProjectInfo
import ru.astrainteractive.gradleplugin.property.model.PublishInfo

val PropertyValue.stringOrNull: String? get() = getValue().getOrNull()
val PropertyValue.stringOrEmpty: String get() = stringOrNull.orEmpty()
val PropertyValue.requireString: String get() = getValue().getOrThrow()

val PropertyValue.int: Result<Int> get() = getValue().mapCatching(String::toInt)
val PropertyValue.intOrNull: Int? get() = int.getOrNull()
val PropertyValue.requireInt: Int get() = int.getOrThrow()

val PropertyValue.boolean: Result<Boolean> get() = getValue().mapCatching(String::toBooleanStrict)
val PropertyValue.booleanOrNull: Boolean? get() = boolean.getOrNull()
val PropertyValue.requireBoolean: Boolean get() = boolean.getOrThrow()

val PropertyValue.jvmTarget: Result<JvmTarget> get() = getValue().mapCatching(JvmTarget::fromTarget)
val PropertyValue.jvmTargetOrNull: JvmTarget get() = jvmTarget.getOrThrow()
val PropertyValue.requireJvmTarget: JvmTarget get() = jvmTarget.getOrThrow()

val PropertyValue.javaVersion: Result<JavaVersion> get() = int.mapCatching(JavaVersion::toVersion)
val PropertyValue.javaVersionOrNull: JavaVersion? get() = javaVersion.getOrNull()
val PropertyValue.requireJavaVersion: JavaVersion get() = javaVersion.getOrThrow()

val Project.requireAndroidSdkInfo: AndroidSdkInfo
    get() = AndroidSdkInfo(
        compile = klibsGradleProperty("android.sdk.compile").requireInt,
        min = klibsGradleProperty("android.sdk.min").requireInt,
        target = klibsGradleProperty("android.sdk.target").requireInt
    )

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

val Project.requireVersionCode: Int
    get() = klibsGradleProperty("project.version.code").requireInt

val Project.requirePublishInfo: PublishInfo
    get() = PublishInfo(
        libraryName = klibsGradleProperty("publish.name").requireString,
        description = klibsGradleProperty("publish.description").requireString,
        gitHubOrganization = klibsGradleProperty("publish.repo.org").requireString,
        gitHubName = klibsGradleProperty("publish.repo.name").requireString,
        license = klibsGradleProperty("publish.license").requireString,
        publishGroupId = klibsGradleProperty("publish.groupId").requireString,
        automaticRelease = klibsGradleProperty("publish.automaticRelease")
            .stringOrNull
            ?.toBooleanStrictOrNull()
            ?: false,
    )

val PropertyValue.developers: Result<List<Developer>>
    get() = getValue().mapCatching(DeveloperMapper::parseDevelopers)

val PropertyValue.requireDevelopers: List<Developer>
    get() = developers.getOrThrow()
