package ru.astrainteractive.gradleplugin.plugin.dokka

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import ru.astrainteractive.gradleplugin.property.util.requireJinfo
import ru.astrainteractive.gradleplugin.property.util.requireProjectInfo

class DokkaRootPlugin : Plugin<Project> {

    private fun applyDokkaForSubProjects(target: Project) {
        target.subprojects.forEach { project ->
            project.pluginManager.apply(DokkaRootPlugin::class.java)
        }
        target.dependencies {
            target.subprojects.forEach { project ->
                add("dokka", project)
            }
        }
    }

    override fun apply(target: Project) {
        with(target.plugins) {
            apply("org.jetbrains.dokka")
        }
        applyDokkaForSubProjects(target)

        target.extensions.configure<DokkaExtension> {
            dokkaGeneratorIsolation.set(ClassLoaderIsolation())
            moduleName.set(target.name)
            moduleVersion.set(target.requireProjectInfo.versionString)

            dokkaPublications.configureEach {
                suppressObviousFunctions.set(true)
                suppressInheritedMembers.set(false)
                includes.from(target.file("README.md"))
            }
            dokkaSourceSets.configureEach {
                jdkVersion.set(target.requireJinfo.jtarget.majorVersion.toInt())
                skipDeprecated.set(false)
                skipEmptyPackages.set(true)
                reportUndocumented.set(false)
                enableJdkDocumentationLink.set(true)
                enableAndroidDocumentationLink.set(true)
                enableKotlinStdLibDocumentationLink.set(true)
                documentedVisibilities(VisibilityModifier.Public)
                perPackageOption {
                    reportUndocumented.set(false)
                }
                if (target.file("README.md").exists()) {
                    includes.from(target.file("README.md"))
                }
            }
            pluginsConfiguration.configureEach {
                when (this) {
                    is DokkaHtmlPluginParameters -> {
//                        footerMessage.set("© ${Config.vendorName}")
//                        customAssets.from(rootDir.resolve("docs/static/icon-512-maskable.png"))
//                        homepageLink.set(Config.url)
                    }
                }
            }
        }
    }
}
