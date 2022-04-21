object PluginsDeps {

    const val kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin_deps}"
    const val tool_build_gradle = "com.android.tools.build:gradle:${Version.build_gradle_deps}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin_deps}"

    const val navigation_safe_args = "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.nav_safe_args}"
    const val dagger_hilt_compiler = "com.google.dagger:hilt-android-gradle-plugin:${Version.dagger_gradle_plugin}"

    object Version {
        const val kotlin_deps = "1.6.10"
        const val build_gradle_deps = "7.0.4"
        const val nav_safe_args = "2.4.2"
        const val dagger_gradle_plugin = "2.38.1"
    }
}