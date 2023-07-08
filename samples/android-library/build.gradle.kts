plugins {
    id("com.android.library")
    id("ru.astrainteractive.gradleplugin.android.core")
    id("ru.astrainteractive.gradleplugin.java.core")
    id("ru.astrainteractive.gradleplugin.detekt")
}
android {
    namespace = libs.versions.project.group.get()
}
