import dependencies.Dependencies
import dependencies.TestDependencies
import dependencies.Versions
import extensions.applyRequiredInjectDeps
import extensions.implementation

plugins {
    id(Plugins.android)
    kotlin(Plugins.android_kotlin)
    kotlin(Plugins.serialization)
    kotlin(Plugins.kapt)
    id(Plugins.dagger_hilt)
}

android {
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.forcecodes.android.gitprofile"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        namespace = "dev.forcecodes.android.gitprofile"
        versionName = getSemanticAppVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            manifestPlaceholders["usesCleartextTraffic"] = true
            isMinifyEnabled = false
            isDebuggable = true
        }

        getByName("release") {
            manifestPlaceholders["usesCleartextTraffic"] = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))

    // Core
    implementation(Dependencies.core_kt)
    implementation(Dependencies.app_compat)
    implementation(Dependencies.material)
    implementation(Dependencies.startup_initializers)

    // Compose
    implementation(Dependencies.compose_ui)
    implementation(Dependencies.compose_material)
    implementation(Dependencies.compose_tooling_preview)
    implementation(Dependencies.compose_navigation)
    implementation(Dependencies.compose_activity)
    implementation(Dependencies.compose_hilt_navigation)
    implementation(Dependencies.compose_coil_kt)
    implementation(Dependencies.compose_foundation)
    implementation(Dependencies.compose_paging)

    // Accompanist
    implementation(Dependencies.accompanist_pager)
    implementation(Dependencies.accompanist_pager_indicator)
    implementation(Dependencies.accompanist_systemuicontroller)
    implementation(Dependencies.accompanist_swipe_refresh)

    implementation(Dependencies.navigation)
    implementation(Dependencies.navigation_ui_ktx)
   // implementation(Dependencies.lifecycle_runtime_ktx)
    implementation(Dependencies.lifecycle_viewmodel)
    implementation(Dependencies.lifecycle_viewmodel_ktx)
    implementation(Dependencies.lifecycle_savedstate)

    implementation(Dependencies.fragment_ktx)
    implementation(Dependencies.swiperefresh)

    implementation(Dependencies.splash_screen)

    implementation(Dependencies.glide)
    kapt(Dependencies.glide_kapt)

    // Toggle switch
    implementation(Dependencies.toggle)

    implementation(Dependencies.hilt_android)
    kapt(Dependencies.hilt_compiler)

    testImplementation(TestDependencies.junit)
    androidTestImplementation(TestDependencies.test_ext)
    androidTestImplementation(TestDependencies.test_rule)
    androidTestImplementation(TestDependencies.test_runner)
    androidTestImplementation(TestDependencies.espresso)
    androidTestImplementation(Dependencies.hilt_android_test)
    androidTestImplementation(TestDependencies.compose_ui_junit)
    debugImplementation(TestDependencies.compose_tooling_test)

    applyRequiredInjectDeps()

    implementation("com.charleskorn.kaml:kaml:0.43.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
}

fun getSemanticAppVersionName(): String {
    val majorCode = 1
    val minorCode = 0
    val patchCode = 0

    return "$majorCode.$minorCode.$patchCode"
}