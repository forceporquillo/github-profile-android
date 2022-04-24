import dependencies.Dependencies
import dependencies.TestDependencies
import dependencies.Versions
import extensions.applyRequiredInjectDeps

plugins {
    id(Plugins.android)
    kotlin(Plugins.android_kotlin)
    kotlin(Plugins.kapt)
    id(Plugins.dagger_hilt)
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "dev.forcecodes.hov"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

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
    implementation(Dependencies.lifecycle_runtime_ktx)
    implementation(Dependencies.lifecycle)
    implementation(Dependencies.fragment_ktx)
    implementation(Dependencies.swiperefresh)

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
}

fun getSemanticAppVersionName(): String {
    val majorCode = 1
    val minorCode = 0
    val patchCode = 0

    return "$majorCode.$minorCode.$patchCode"
}