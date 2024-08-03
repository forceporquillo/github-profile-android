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
        release {
            isMinifyEnabled = false
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
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":data")))

    testImplementation("junit:junit:4.13.2")

    implementation(Dependencies.hilt_android)
    kapt(Dependencies.hilt_compiler)
}