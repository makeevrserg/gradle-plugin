package ru.astrainteractive.gradleplugin.plugin.linter.model

/**
 * One violation of the architecture rules, from the Konsist checks or the structure checks.
 *
 * @property path the file or directory, relative to the project directory
 */
internal data class ArchitectureFinding(
    val rule: String,
    val path: String,
    val subject: String,
    val message: String
) {
    /**
     * Identifies the finding in a baseline: the message may change wording, the rule, path and subject do not.
     */
    val baselineKey: String
        get() = listOf(rule, path, subject).joinToString(separator = FIELD_SEPARATOR)

    fun describe(): String = "$path: $message [$rule]"

    companion object {
        const val FIELD_SEPARATOR = "\t"
        private const val FIELD_COUNT = 4

        /**
         * Parses a line of the Konsist report, `rule<TAB>path<TAB>subject<TAB>message`.
         */
        fun parse(line: String): ArchitectureFinding {
            val fields = line.split(FIELD_SEPARATOR, limit = FIELD_COUNT)
            require(fields.size == FIELD_COUNT) { "Malformed architecture report line: '$line'" }
            return ArchitectureFinding(rule = fields[0], path = fields[1], subject = fields[2], message = fields[3])
        }
    }
}
