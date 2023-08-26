import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import extensions.applyDefaults

/* BUILD SCRIPT */

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        classpath(PluginsDeps.kotlin_serialization)
        classpath(PluginsDeps.kotlin_gradle_plugin)
        classpath(PluginsDeps.tool_build_gradle)
        classpath(PluginsDeps.navigation_safe_args)
        classpath(PluginsDeps.dagger_hilt_compiler)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
    }
}

allprojects {
    repositories.applyDefaults()
}

subprojects {
    tasks.withType<KotlinCompile>().all {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlin.OptIn",
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xuse-experimental=kotlinx.coroutines.ObsoleteCoroutinesApi",
            "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
        )
    }

    tasks.withType<Test> {
        testLogging {
            // set options for log level LIFECYCLE
            events = setOf(FAILED, STARTED, PASSED, SKIPPED, STANDARD_OUT)
            exceptionFormat = FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }

        maxParallelForks =
            (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}