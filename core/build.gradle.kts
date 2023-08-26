import dependencies.Dependencies

plugins {
    id(Plugins.android_library)
    kotlin(Plugins.android_kotlin)
    kotlin(Plugins.kapt)
}


android {
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(Dependencies.hilt_android)
    kapt(Dependencies.hilt_compiler)

    api(Dependencies.kotlin_coroutines_core)
    api(Dependencies.kotlin_stdlib)
    api(Dependencies.kotlin_coroutines_android)

    api(Dependencies.paging)
    api(Dependencies.timber)
}