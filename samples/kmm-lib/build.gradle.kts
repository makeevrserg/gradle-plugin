plugins {
    id("kmm-library-convention")
    id("dokka-convention")
    id("com.makeevrserg.gradleplugin.detekt")
}
android {
    namespace = libs.versions.project.group.get()
}
