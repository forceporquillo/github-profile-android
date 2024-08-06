@file:Suppress("ConstPropertyName")

object PluginsDeps {

    const val kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin_deps}"
    const val tool_build_gradle = "com.android.tools.build:gradle:${Version.build_gradle_deps}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin_deps}"

    const val navigation_safe_args = "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.nav_safe_args}"
    const val dagger_hilt_compiler = "com.google.dagger:hilt-android-gradle-plugin:${Version.dagger_gradle_plugin}"

    const val ksp = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Version.ksp_version}"

    object Version {
        const val ksp_version = "1.9.10-1.0.13"
        const val kotlin_deps = "1.9.10"
        const val build_gradle_deps = "8.5.1"
        const val nav_safe_args = "2.5.0"
        const val dagger_gradle_plugin = "2.48"
    }
}