import com.makeevrserg.gradleplugin.ApplicationVariantAction
import com.makeevrserg.gradleplugin.ConventionProject

plugins {
    id("com.android.application")
    id("kotlin-android")
}
java {
    java.sourceCompatibility = ConventionProject.JAVA_VERSION
    java.targetCompatibility = ConventionProject.JAVA_VERSION
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = ConventionProject.JAVA_VERSION.majorVersion
}
android {
    compileSdk = libs.versions.project.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.project.sdk.min.get().toInt()
        targetSdk = libs.versions.project.sdk.target.get().toInt()
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = ConventionProject.JAVA_VERSION
        targetCompatibility = ConventionProject.JAVA_VERSION
    }
    kotlinOptions {
        jvmTarget = ConventionProject.JAVA_VERSION.majorVersion
    }
    buildTypes {
        applicationVariants.all(ApplicationVariantAction(project))
    }
}
