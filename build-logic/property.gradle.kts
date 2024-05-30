import java.io.InputStream
import java.util.Properties

interface PropertyValue {
    val value: String
}

class GradleProperty(
    private val property: String,
) : PropertyValue {
    override val value: String
        get() {
            val value = project.property(property)?.toString()
            if (value.isNullOrEmpty()) throw GradleException("Property $property not found!")
            return value
        }
}

class SecretProperty(
    private val property: String,
) : PropertyValue {
    private val envProperty = property
    private val gradleProperty = "makeevrserg.$property"

    private fun findEnvValue(): String? {
        val envValue = System.getenv(envProperty)?.toString()
        if (envValue.isNullOrEmpty()) {
            project.logger.error("Enviroment $envProperty property missing, getting from local.properties")
        } else {
            project.logger.error("Got $envProperty property from enviroment")
        }
        return envValue
    }

    private fun requireLocalPropertyValue(): String {
        val properties = Properties().apply {
            val localProperties = project.rootProject.file("local.properties")
            if (!localProperties.exists()) return "EMPTY_PROPERTY_VALUE"
            val inputStream: InputStream = localProperties.inputStream()
            load(inputStream)
        }
        project.logger.info("Got $property from local properties")
        return properties.getProperty(gradleProperty) ?: "EMPTY_PROPERTY_VALUE"
    }

    override val value: String
        get() = findEnvValue() ?: requireLocalPropertyValue()
}

val requireProjectInfo: Map<String, String>
    get() = mapOf(
        "project.name" to GradleProperty("project.name").value,
        "project.web" to GradleProperty("project.web").value,
        "project.group" to GradleProperty("project.group").value,
        "project.version" to GradleProperty("project.version").value,
        "project.description" to GradleProperty("project.description").value,
        "secret.ossrhUsername" to SecretProperty("OSSRH_USERNAME").value,
        "secret.osshPassword" to SecretProperty("OSSRH_PASSWORD").value,
        "secret.signingKey" to SecretProperty("SIGNING_KEY").value,
        "secret.signingKeyId" to SecretProperty("SIGNING_KEY_ID").value,
        "secret.signingPassword" to SecretProperty("SIGNING_PASSWORD").value,
    )

requireProjectInfo.forEach { (k, v) ->
    extensions.add(k, v)
    extra.set(k, v)
}
