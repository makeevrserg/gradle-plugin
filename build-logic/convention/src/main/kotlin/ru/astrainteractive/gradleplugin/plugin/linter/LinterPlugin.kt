package ru.astrainteractive.gradleplugin.plugin.linter

import dev.detekt.gradle.Detekt
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvableConfiguration
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.java.TargetJvmEnvironment
import org.gradle.api.file.FileCollection
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerIfAbsent
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import ru.astrainteractive.gradleplugin.plugin.detekt.DetektConfigService
import ru.astrainteractive.gradleplugin.plugin.detekt.DetektPlugin
import java.util.Properties

/**
 * One linter for the architecture rules: detekt with the shared configuration, the architecture detekt rules
 * and their settings, and `architectureCheck`, which runs the Konsist checks of the unit's package layout and
 * the structure checks of its directory and dependencies. `check` depends on both.
 */
class LinterPlugin : Plugin<Project> {
    private fun readResource(name: String): String {
        val stream = javaClass.classLoader.getResourceAsStream(name)
            ?: throw GradleException("$name is missing from the convention plugin jar")
        return stream.use { input -> input.readBytes().decodeToString() }
    }

    private fun readRulesVersion(): String {
        val properties = Properties()
        properties.load(readResource(RULES_VERSION_FILE).reader())
        return properties.getProperty("version")
    }

    private fun Project.explicitApiArguments(): List<String> {
        if (!hasKotlinPlugin()) return emptyList()
        val mode = explicitApiMode() ?: return emptyList()
        return listOf("-Xexplicit-api=$mode")
    }

    private fun Project.configureDetekt(rulesVersion: String) {
        dependencies.add("detektPlugins", "$RULES_GROUP:detekt:$rulesVersion")
        val configService = gradle.sharedServices.registerIfAbsent(
            name = CONFIG_SERVICE_NAME,
            implementationType = DetektConfigService::class,
            configureAction = {
                parameters.content.set(provider { readResource(ARCHITECTURE_CONFIG_FILE_NAME) })
                parameters.configFile.fileValue(
                    isolated.rootProject.projectDirectory.file("build/detekt/$ARCHITECTURE_CONFIG_FILE_NAME").asFile
                )
            }
        )
        val architectureConfigFile = configService.map { service -> service.configFile }
        val explicitApiArguments = provider { explicitApiArguments() }
        tasks.withType<Detekt>().configureEach {
            usesService(configService)
            config.from(architectureConfigFile)
            freeCompilerArgs.addAll(explicitApiArguments)
        }
    }

    private fun Project.konsistClasspath(rulesVersion: String): NamedDomainObjectProvider<ResolvableConfiguration> {
        val konsist = configurations.dependencyScope(KONSIST_CONFIGURATION) {
            dependencies.add(project.dependencies.create("$RULES_GROUP:konsist:$rulesVersion"))
        }
        return configurations.resolvable("${KONSIST_CONFIGURATION}Classpath") {
            extendsFrom(konsist.get())
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(
                    TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                    objects.named(TargetJvmEnvironment.STANDARD_JVM)
                )
            }
        }
    }

    private fun Project.architectureInputs(): FileCollection = fileTree(projectDir) {
        include("src/**/*.kt", "src/**/*.java", "README.md", "**/build.gradle.kts", "**/build.gradle")
        exclude("**/build/**", "**/.gradle/**")
    }

    private fun ArchitectureTask.configureFrom(
        project: Project,
        extension: LinterExtension,
        classpath: NamedDomainObjectProvider<ResolvableConfiguration>
    ) {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        konsistClasspath.from(classpath)
        sources.from(project.architectureInputs())
        projectDirectory.set(project.layout.projectDirectory)
        projectPath.set(project.path)
        unitKind.set(extension.unitKind)
        rootPackage.set(extension.rootPackage)
        rootOfBuild.set(project.path == project.isolated.rootProject.path)
        projectDependencies.set(project.provider { project.productionProjectDependencies() })
    }

    override fun apply(target: Project) {
        target.pluginManager.apply(DetektPlugin::class.java)
        val rulesVersion = readRulesVersion()
        target.configureDetekt(rulesVersion)

        val extension = target.extensions.create<LinterExtension>(EXTENSION_NAME)
        extension.unitKind.convention(target.provider { target.detectUnitKind() })
        val classpath = target.konsistClasspath(rulesVersion)

        val checkTask = target.tasks.register<ArchitectureCheckTask>(CHECK_TASK_NAME) {
            description = "Checks the package layout and the unit structure against the architecture rules"
            configureFrom(target, extension, classpath)
            baseline.from(target.layout.projectDirectory.file(BASELINE_FILE_NAME))
            reportFile.set(target.layout.buildDirectory.file("reports/architecture/violations.txt"))
        }
        target.tasks.register<ArchitectureBaselineTask>(BASELINE_TASK_NAME) {
            description = "Accepts the current architecture violations in $BASELINE_FILE_NAME"
            configureFrom(target, extension, classpath)
            baselineFile.set(target.layout.projectDirectory.file(BASELINE_FILE_NAME))
        }
        target.plugins.withType<LifecycleBasePlugin> {
            target.tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) { dependsOn(checkTask) }
        }
    }

    companion object {
        const val CHECK_TASK_NAME = "architectureCheck"
        const val BASELINE_TASK_NAME = "architectureBaseline"
        const val BASELINE_FILE_NAME = "architecture-baseline.txt"

        private const val EXTENSION_NAME = "linter"
        private const val RULES_GROUP = "ru.astrainteractive.klibs.rules"
        private const val RULES_VERSION_FILE = "klibs-rules.properties"
        private const val ARCHITECTURE_CONFIG_FILE_NAME = "detekt-architecture.yml"
        private const val CONFIG_SERVICE_NAME = "klibsDetektArchitectureConfig"
        private const val KONSIST_CONFIGURATION = "klibsArchitectureKonsist"
    }
}
