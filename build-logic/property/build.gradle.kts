plugins {
    `kotlin-dsl`
    id("com.vanniktech.maven.publish")
}

dependencies {
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.kotlin.gradle)
}
