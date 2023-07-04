plugins {
    id("kmm-library-convention")
    id("dokka-convention")
    id("com.makeevrserg.gradleplugin.detekt")
    id("com.makeevrserg.gradleplugin.android.core")
    id("com.makeevrserg.gradleplugin.java.core")
}
android {
    namespace = libs.versions.project.group.get()
}
