plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}
dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.toolsBuild)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.lint.detekt.gradle)
    implementation(libs.dokka.android)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.base)
}
gradlePlugin {
    plugins {
        create("detekt") {
            id = "${libs.versions.project.group.get()}.detekt"
            implementationClass = "${libs.versions.project.group.get()}.DetektPlugin"
        }
    }
}
