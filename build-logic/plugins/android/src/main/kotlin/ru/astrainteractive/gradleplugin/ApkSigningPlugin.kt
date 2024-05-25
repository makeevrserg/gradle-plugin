package ru.astrainteractive.gradleplugin

import com.android.build.gradle.AbstractAppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradleplugin.property.PropertyValue.Companion.baseSecretProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.stringOrEmpty

class ApkSigningPlugin : Plugin<Project> {
    private val Project.keyPassword: String
        get() = baseSecretProperty(KEY_PASSWORD_PATH).stringOrEmpty
    private val Project.keyAlias: String
        get() = baseSecretProperty(KEY_ALIAS_PATH).stringOrEmpty
    private val Project.storePassword: String
        get() = baseSecretProperty(STORE_PASSWORD_PATH).stringOrEmpty

    override fun apply(target: Project) {
        target.configure<AbstractAppExtension> {
            signingConfigs {
                val secretKeyAlias = target.keyAlias
                val secretKeyPassword = target.keyPassword
                val secretStorePassword = target.storePassword

                val signingFile = target.file(SIGNING_FILE_NAME)

                if (!signingFile.exists()) {
                    target.logger.error("Signing file named $SIGNING_FILE_NAME not found in ${target.path}")
                    return@signingConfigs
                }
                if (secretKeyAlias.isEmpty()) {
                    target.logger.error("Secret key named $KEY_ALIAS_PATH in local.properties is empty")
                    return@signingConfigs
                }

                if (secretKeyPassword.isEmpty()) {
                    target.logger.error("Secret key named $KEY_PASSWORD_PATH in local.properties is empty")
                    return@signingConfigs
                }

                if (secretStorePassword.isEmpty()) {
                    target.logger.error("Secret key named $STORE_PASSWORD_PATH in local.properties is empty")
                    return@signingConfigs
                }

                getByName("debug") {
                    keyAlias = secretKeyAlias
                    keyPassword = secretKeyPassword
                    storePassword = secretStorePassword
                    storeFile = signingFile
                }
                create("release") {
                    keyAlias = secretKeyAlias
                    keyPassword = secretKeyPassword
                    storePassword = secretStorePassword
                    storeFile = signingFile
                }
            }
            buildTypes {
                named("release") {
                    signingConfig = signingConfigs.getByName("release")
                }
                named("debug") {
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
