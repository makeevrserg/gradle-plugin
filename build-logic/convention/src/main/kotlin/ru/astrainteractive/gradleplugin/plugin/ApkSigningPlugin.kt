package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.ApplicationVariantDimension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryVariantDimension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradle.property.api.secretProperty
import ru.astrainteractive.gradleplugin.property.util.stringOrEmpty

class ApkSigningPlugin : Plugin<Project> {
    private val Project.keyPassword: String
        get() = secretProperty(KEY_PASSWORD_PATH).stringOrEmpty
    private val Project.keyAlias: String
        get() = secretProperty(KEY_ALIAS_PATH).stringOrEmpty
    private val Project.storePassword: String
        get() = secretProperty(STORE_PASSWORD_PATH).stringOrEmpty

    override fun apply(target: Project) {
        target.extensions.configure<CommonExtension> {
            val secretKeyAlias = target.keyAlias
            val secretKeyPassword = target.keyPassword
            val secretStorePassword = target.storePassword

            val signingFile = target.file(SIGNING_FILE_NAME)

            val missingSecrets = listOfNotNull(
                KEY_ALIAS_PATH.takeIf { secretKeyAlias.isEmpty() },
                KEY_PASSWORD_PATH.takeIf { secretKeyPassword.isEmpty() },
                STORE_PASSWORD_PATH.takeIf { secretStorePassword.isEmpty() }
            )
            if (missingSecrets.isNotEmpty()) {
                target.logger.error(
                    "Signing secrets ${missingSecrets.joinToString()} are not defined " +
                        "in the environment or local.properties of ${target.path}"
                )
                return@configure
            }

            signingConfigs.getByName("debug") {
                keyAlias = secretKeyAlias
                keyPassword = secretKeyPassword
                storePassword = secretStorePassword
                storeFile = signingFile
            }
            signingConfigs.create("release") {
                keyAlias = secretKeyAlias
                keyPassword = secretKeyPassword
                storePassword = secretStorePassword
                storeFile = signingFile
            }

            buildTypes.named("release") {
                if (this is LibraryVariantDimension) {
                    signingConfig = signingConfigs.getByName("release")
                }
                if (this is ApplicationVariantDimension) {
                    signingConfig = signingConfigs.getByName("release")
                }
            }
            buildTypes.named("debug") {
                if (this is LibraryVariantDimension) {
                    signingConfig = signingConfigs.getByName("debug")
                }
                if (this is ApplicationVariantDimension) {
                    signingConfig = signingConfigs.getByName("debug")
                }
            }
        }
    }

    companion object {
        private const val SIGNING_FILE_NAME = "keystore.jks"
        private const val KEY_PASSWORD_PATH = "KEY_PASSWORD"
        private const val KEY_ALIAS_PATH = "KEY_ALIAS"
        private const val STORE_PASSWORD_PATH = "STORE_PASSWORD"
    }
}
