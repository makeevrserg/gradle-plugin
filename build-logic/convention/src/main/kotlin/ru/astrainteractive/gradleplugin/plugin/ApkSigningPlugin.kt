package ru.astrainteractive.gradleplugin.plugin

import com.android.build.api.dsl.ApplicationVariantDimension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryVariantDimension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.astrainteractive.gradle.property.api.klibsSecretProperty
import ru.astrainteractive.gradleplugin.property.extension.PrimitivePropertyValueExt.stringOrEmpty

class ApkSigningPlugin : Plugin<Project> {
    private val Project.keyPassword: String
        get() = klibsSecretProperty(KEY_PASSWORD_PATH).stringOrEmpty
    private val Project.keyAlias: String
        get() = klibsSecretProperty(KEY_ALIAS_PATH).stringOrEmpty
    private val Project.storePassword: String
        get() = klibsSecretProperty(STORE_PASSWORD_PATH).stringOrEmpty

    override fun apply(target: Project) {
        target.extensions.configure<CommonExtension> {
            val secretKeyAlias = target.keyAlias
            val secretKeyPassword = target.keyPassword
            val secretStorePassword = target.storePassword

            val signingFile = target.file(SIGNING_FILE_NAME)

            if (!signingFile.exists()) {
                target.logger.error("Signing file named $SIGNING_FILE_NAME not found in ${target.path}")
                return@configure
            }
            if (secretKeyAlias.isEmpty()) {
                target.logger.error("Secret key named $KEY_ALIAS_PATH in local.properties is empty")
                return@configure
            }

            if (secretKeyPassword.isEmpty()) {
                target.logger.error("Secret key named $KEY_PASSWORD_PATH in local.properties is empty")
                return@configure
            }

            if (secretStorePassword.isEmpty()) {
                target.logger.error("Secret key named $STORE_PASSWORD_PATH in local.properties is empty")
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
