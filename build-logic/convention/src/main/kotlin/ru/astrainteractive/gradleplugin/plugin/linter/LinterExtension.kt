package ru.astrainteractive.gradleplugin.plugin.linter

import org.gradle.api.provider.Property

/**
 * Overrides what the linter detects about the build unit.
 */
abstract class LinterExtension {
    /**
     * One of `context`, `library`, `api`, `contract`, `entry-point`. Detected by convention: the path segment
     * `api` or `contract`, an application plugin or a path under `:instances` for an entry point, explicit API
     * strict for a library, a context otherwise.
     */
    abstract val unitKind: Property<String>

    /**
     * The unit's root package; detected from the sources when not set, as their common package without the
     * trailing role segments.
     */
    abstract val rootPackage: Property<String>
}
