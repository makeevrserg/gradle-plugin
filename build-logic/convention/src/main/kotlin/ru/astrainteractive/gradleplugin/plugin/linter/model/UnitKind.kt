package ru.astrainteractive.gradleplugin.plugin.linter.model

/**
 * The kind of build unit, which decides the layout rules the Konsist checks apply.
 *
 * @property cliName the value of `linter.unitKind` and of the Konsist `--unit-kind` option
 */
internal enum class UnitKind(val cliName: String) {
    CONTEXT("context"),
    LIBRARY("library"),
    API("api"),
    CONTRACT("contract"),
    ENTRY_POINT("entry-point");

    companion object {
        fun fromCliName(cliName: String): UnitKind? = entries.find { kind -> kind.cliName == cliName }
    }
}
