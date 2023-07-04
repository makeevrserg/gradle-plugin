plugins {
    id("android-app-convention")
    id("com.makeevrserg.gradleplugin.android.core")
    id("com.makeevrserg.gradleplugin.java.core")
}
android {
    namespace = libs.versions.project.group.get()
}
