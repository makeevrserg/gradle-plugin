plugins {
    id("com.android.application")
    id("ru.astrainteractive.gradleplugin.android.core")
    id("ru.astrainteractive.gradleplugin.java.core")
    id("ru.astrainteractive.gradleplugin.android.apk.name")
    id("ru.astrainteractive.gradleplugin.detekt.compose")
}
android {
    namespace = libs.versions.project.group.get()
}
