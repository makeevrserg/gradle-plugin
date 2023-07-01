import com.makeevrserg.gradleplugin.ConventionProject
import org.jetbrains.dokka.gradle.DokkaTaskPartial

group = libs.versions.project.group.get()
version = libs.versions.project.version.string.get()

plugins {
    id("org.jetbrains.dokka")
}
tasks.dokkaHtml.configure {
    moduleName.set(project.name)
    suppressObviousFunctions.set(false)

    dokkaSourceSets.configureEach {
        jdkVersion.set(ConventionProject.JAVA_VERSION.majorVersion.toInt())
        includeNonPublic.set(false)
        skipDeprecated.set(false)
        reportUndocumented.set(true)
        skipEmptyPackages.set(true)
        perPackageOption {
            reportUndocumented.set(false)
        }
    }
}
tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        if (project.file("README.md").exists()) {
            includes.from()
        }
    }
}
