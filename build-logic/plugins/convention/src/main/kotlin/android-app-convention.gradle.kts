import com.makeevrserg.gradleplugin.ApplicationVariantAction

plugins {
    id("com.android.application")
    id("kotlin-android")
}
android {
    defaultConfig {
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        applicationVariants.all(ApplicationVariantAction(project))
    }
}
