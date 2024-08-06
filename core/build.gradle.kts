import dependencies.Dependencies

plugins {
    id(Plugins.android_library)
    kotlin(Plugins.android_kotlin)
    id(Plugins.ksp)
}


android {
    compileSdk = 35

    buildFeatures.buildConfig = true

    defaultConfig {
        minSdk = 24
        namespace = "dev.forcecodes.gitprofile.core"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(Dependencies.hilt_android)
    ksp(Dependencies.hilt_compiler)

    api(Dependencies.kotlin_coroutines_core)
    api(Dependencies.kotlin_stdlib)
    api(Dependencies.kotlin_coroutines_android)

    api(Dependencies.paging)
    api(Dependencies.timber)
}