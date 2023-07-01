import com.makeevrserg.gradleplugin.ConventionProject.KEY_ALIAS
import com.makeevrserg.gradleplugin.ConventionProject.KEY_PASSWORD
import com.makeevrserg.gradleplugin.ConventionProject.STORE_PASSWORD

plugins {
    id("android-app-convention")
}

android {
    signingConfigs {
        getByName("debug") {
            if (file("keystore.jks").exists()) {
                keyAlias = KEY_ALIAS
                keyPassword = KEY_PASSWORD
                storePassword = STORE_PASSWORD
                storeFile = file("keystore.jks")
            }
        }
        create("release") {
            if (file("keystore.jks").exists()) {
                keyAlias = KEY_ALIAS
                keyPassword = KEY_PASSWORD
                storePassword = STORE_PASSWORD
                storeFile = file("keystore.jks")
            }
        }
    }
}
