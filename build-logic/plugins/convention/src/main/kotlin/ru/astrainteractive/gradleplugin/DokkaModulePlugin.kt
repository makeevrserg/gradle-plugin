package ru.astrainteractive.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireJinfo

class DokkaModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.plugins) {
            apply("org.jetbrains.dokka")
        }
        target.tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml").configure {
            moduleName.set(project.name)
            suppressObviousFunctions.set(false)

            dokkaSourceSets.configureEach {
                jdkVersion.set(target.requireJinfo.jtarget.majorVersion.toInt())
                includeNonPublic.set(false)
                skipDeprecated.set(false)
                reportUndocumented.set(true)
                skipEmptyPackages.set(true)
                perPackageOption {
                    reportUndocumented.set(false)
                }
            }
        }
        target.tasks.withType<DokkaTaskPartial>().configureEach {
            dokkaSourceSets.configureEach {
                if (project.file("README.md").exists()) {
                    includes.from()
                }
            }
        }
    }
}
