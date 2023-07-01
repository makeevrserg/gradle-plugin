plugins {
    id("android-library-convention")
}
android {
    namespace = libs.versions.project.group.get()
}
