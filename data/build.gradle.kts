import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import dependencies.Dependencies
import dependencies.TestDependencies
import extensions.applyRequiredInjectDeps
import extensions.implementation

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
        namespace = "dev.forcecodes.gitprofile.data"

        consumerProguardFiles("consumer-rules.pro")

        val properties = gradleLocalProperties(rootDir, providers)

        val username = properties.getProperty("USERNAME", "")
        val token = properties.getProperty("TOKEN", "")

        buildConfigField("String", "USERNAME", "\"$username\"")
        buildConfigField("String", "TOKEN", "\"$token\"")
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

    configurations.all {
        resolutionStrategy {
            force("org.xerial:sqlite-jdbc:3.34.0")
        }
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(mapOf("path" to ":core")))

    implementation(libs.hilt.android)
    ksp(Dependencies.hilt_compiler)

    applyRequiredInjectDeps()
    ksp(Dependencies.room_compiler)
    ksp(Dependencies.moshi_code_gen)

    implementation(Dependencies.datastore_prefs) {
        exclude("androidx.lifecycle", "lifecycle-viewmodel")
        exclude("androidx.lifecycle", "lifecycle-viewmodel-ktx")
    }

    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.test_ext)
    testImplementation(TestDependencies.mockito_kotlin)

    implementation(Dependencies.room_paging) {
        exclude("androidx.lifecycle", "lifecycle-viewmodel")
        exclude("androidx.lifecycle", "lifecycle-viewmodel-ktx")
    }
}