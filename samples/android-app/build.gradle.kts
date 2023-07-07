plugins {
    id("com.android.application")
    id("com.makeevrserg.gradleplugin.android.core")
    id("com.makeevrserg.gradleplugin.java.core")
    id("com.makeevrserg.gradleplugin.android.apk.name")
    id("com.makeevrserg.gradleplugin.detekt.compose")
}
android {
    namespace = libs.versions.project.group.get()
}
