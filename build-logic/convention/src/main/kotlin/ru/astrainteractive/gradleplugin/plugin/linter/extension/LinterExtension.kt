package ru.astrainteractive.gradleplugin.plugin.linter.extension

import org.gradle.api.provider.Property

/**
 * Overrides what the linter detects about the build unit.
 */
abstract class LinterExtension {
    /**
     * One of `context`, `library`, `api`, `contract`, `entry-point`, `technology`. Detected by convention: the
     * path segment `api` or `contract`, a technology name (`data/exposed`) for a technology unit, an application
     * plugin or a path under `:instances` for an entry point, explicit API strict for a library, a context
     * otherwise.
     */
    abstract val unitKind: Property<String>

    /**
     * The unit's root package; detected when not set, as the common package of the sources cut after the
     * unit's name (`:modules:event-bukkit` ends at `…event`).
     */
    abstract val rootPackage: Property<String>
}
