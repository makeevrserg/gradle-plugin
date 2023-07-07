plugins {
    id("com.android.library")
    id("com.makeevrserg.gradleplugin.android.core")
    id("com.makeevrserg.gradleplugin.java.core")
    id("com.makeevrserg.gradleplugin.detekt")
}
android {
    namespace = libs.versions.project.group.get()
}
