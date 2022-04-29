package extensions

import dependencies.Dependencies
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.applyRequiredInjectDeps() {
    implementation(Dependencies.room_runtime)
    implementation(Dependencies.room_ktx)

    implementation(Dependencies.retrofit)
    implementation(Dependencies.okhttp_urlconnection)
    implementation(Dependencies.moshi_converter)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttp_logging)
    implementation(Dependencies.moshi)

    implementation(Dependencies.datastore_prefs)
}

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)