package ru.astrainteractive.gradleplugin.util

import org.gradle.api.GradleException
import org.gradle.api.Project
import ru.astrainteractive.gradleplugin.models.Developer

class GradleProperty(path: String, private val project: Project) : BaseProperty("$BASE_PREFIX.$path") {

    @Suppress("MemberNameEqualsClassName")
    override val anyProperty: Any
        get() = project.property(property) ?: throw GradleException("Required property $property not defined!")

    private fun parseDeveloper(text: String): Developer {
        val items: List<String> = text.split("|").map { it.trim() }
        if (items.size != 3) {
            throw GradleException(
                "Developer profile should have 3 parts with | delimiter. For example: makeevrserg|Makeev Roman|makeevrserg@gmail.com"
            )
        }
        return Developer(
            id = items[0],
            name = items[1],
            email = items[2]
        )
    }

    val developers: List<Developer>
        get() = withCatching { string.split(",").map(::parseDeveloper) }

    companion object {
        fun Project.gradleProperty(path: String) = GradleProperty(path, this)
    }
}
