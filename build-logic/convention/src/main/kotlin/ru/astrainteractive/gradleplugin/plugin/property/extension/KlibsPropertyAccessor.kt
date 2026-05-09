package ru.astrainteractive.gradleplugin.plugin.property.extension

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.astrainteractive.gradle.property.api.PropertyValue
import ru.astrainteractive.gradleplugin.property.model.Developer
import ru.astrainteractive.gradleplugin.property.util.boolean
import ru.astrainteractive.gradleplugin.property.util.booleanOrNull
import ru.astrainteractive.gradleplugin.property.util.developers
import ru.astrainteractive.gradleplugin.property.util.int
import ru.astrainteractive.gradleplugin.property.util.intOrNull
import ru.astrainteractive.gradleplugin.property.util.javaVersion
import ru.astrainteractive.gradleplugin.property.util.javaVersionOrNull
import ru.astrainteractive.gradleplugin.property.util.jvmTarget
import ru.astrainteractive.gradleplugin.property.util.jvmTargetOrNull
import ru.astrainteractive.gradleplugin.property.util.requireBoolean
import ru.astrainteractive.gradleplugin.property.util.requireInt
import ru.astrainteractive.gradleplugin.property.util.requireJavaVersion
import ru.astrainteractive.gradleplugin.property.util.requireJvmTarget
import ru.astrainteractive.gradleplugin.property.util.requireString
import ru.astrainteractive.gradleplugin.property.util.stringOrEmpty
import ru.astrainteractive.gradleplugin.property.util.stringOrNull

/**
 * Thin wrapper around [PropertyValue] that exposes all conversion helpers as
 * **member** functions/properties.
 *
 * Member access avoids the need for `import` statements in `build.gradle.kts`,
 * unlike the `ru.astrainteractive.gradleplugin.property.util.*` extension
 * properties.
 */
class KlibsPropertyAccessor internal constructor(
    private val delegate: PropertyValue
) : PropertyValue by delegate {

    val stringOrNull: String? get() = delegate.stringOrNull
    val stringOrEmpty: String get() = delegate.stringOrEmpty
    val requireString: String get() = delegate.requireString

    val int: Result<Int> get() = delegate.int
    val intOrNull: Int? get() = delegate.intOrNull
    val requireInt: Int get() = delegate.requireInt

    val boolean: Result<Boolean> get() = delegate.boolean
    val booleanOrNull: Boolean? get() = delegate.booleanOrNull
    val requireBoolean: Boolean get() = delegate.requireBoolean

    val jvmTarget: Result<JvmTarget> get() = delegate.jvmTarget
    val jvmTargetOrNull: JvmTarget? get() = delegate.jvmTargetOrNull
    val requireJvmTarget: JvmTarget get() = delegate.requireJvmTarget

    val javaVersion: Result<JavaVersion> get() = delegate.javaVersion
    val javaVersionOrNull: JavaVersion? get() = delegate.javaVersionOrNull
    val requireJavaVersion: JavaVersion get() = delegate.requireJavaVersion

    val developers: Result<List<Developer>> get() = delegate.developers
    val requireDevelopers: List<Developer> get() = developers.getOrThrow()
}
