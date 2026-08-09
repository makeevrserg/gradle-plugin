plugins {
    `kotlin-dsl`
    id("klibs.module")
}

dependencies {
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.kotlin.gradle)
}
