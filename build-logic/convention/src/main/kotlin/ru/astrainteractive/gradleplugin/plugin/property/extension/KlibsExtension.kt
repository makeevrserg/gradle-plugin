package ru.astrainteractive.gradleplugin.plugin.property.extension

import org.gradle.api.Project
import ru.astrainteractive.gradle.property.api.gradleProperty
import ru.astrainteractive.gradle.property.api.klibsGradleProperty
import ru.astrainteractive.gradle.property.api.secretProperty
import ru.astrainteractive.gradleplugin.property.model.AndroidSdkInfo
import ru.astrainteractive.gradleplugin.property.model.JInfo
import ru.astrainteractive.gradleplugin.property.model.ProjectInfo
import ru.astrainteractive.gradleplugin.property.model.PublishInfo
import ru.astrainteractive.gradleplugin.property.util.requireAndroidSdkInfo
import ru.astrainteractive.gradleplugin.property.util.requireJinfo
import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo
import ru.astrainteractive.gradleplugin.property.util.requirePublishInfo
import ru.astrainteractive.gradleplugin.property.util.requireVersionCode
import javax.inject.Inject

/**
 * Project extension exposing all `gradle.properties`-derived values as
 * **member** properties/functions.
 *
 * Once registered via [ru.astrainteractive.gradleplugin.plugin.property.KlibsExtensionPlugin],
 * Gradle generates a type-safe `klibs` accessor for `Project`, so consumers can use:
 *
 * ```kotlin
 * android {
 *     namespace = klibs.projectInfo.group
 *     defaultConfig {
 *         versionCode = klibs.versionCode
 *         versionName = klibs.projectInfo.versionString
 *     }
 * }
 *
 * tasks.register<SecretFileTask>("exportKeystore") {
 *     base64 = klibs.secret("KEYSTORE_BASE64").stringOrEmpty
 * }
 * ```
 *
 * **without any imports** in `build.gradle.kts`.
 */
abstract class KlibsExtension @Inject constructor(
    private val project: Project
) {
    /** Project info from `klibs.project.*` properties. */
    val projectInfo: ProjectInfo get() = project.requireProjectInfo

    /** Java/Kotlin target info from `klibs.java.*` properties. */
    val jinfo: JInfo get() = project.requireJinfo

    /** Android SDK levels from `klibs.android.sdk.*` properties. */
    val androidSdkInfo: AndroidSdkInfo get() = project.requireAndroidSdkInfo

    /** Maven publication info from `klibs.publish.*` properties. */
    val publishInfo: PublishInfo get() = project.requirePublishInfo

    /** Shorthand for `klibs.project.version.code`. */
    val versionCode: Int get() = project.requireVersionCode

    /**
     * Access an arbitrary `klibs.*` property by its sub-path.
     * Example: `klibs.property("project.version.code").requireInt`.
     */
    fun klibsGradleProperty(path: String): KlibsPropertyAccessor {
        return KlibsPropertyAccessor(project.klibsGradleProperty(path))
    }

    /**
     * Access an arbitrary raw gradle property by absolute key.
     * Example: `klibs.rawProperty("my.custom.key").stringOrNull`.
     */
    fun gradleProperty(key: String): KlibsPropertyAccessor {
        return KlibsPropertyAccessor(project.gradleProperty(key))
    }

    /**
     * Access a value from the local `secret.properties` file.
     * Example: `klibs.secret("KEYSTORE_BASE64").stringOrEmpty`.
     */
    fun secretProperty(key: String): KlibsPropertyAccessor {
        return KlibsPropertyAccessor(project.secretProperty(key))
    }
}
