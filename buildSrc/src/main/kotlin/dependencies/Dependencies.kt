@file:Suppress("ConstPropertyName")

package dependencies

object Dependencies {

    // Startup
    const val startup_initializers = "androidx.startup:startup-runtime:${Versions.app_initializers}"

    // SplashScreen
    const val splash_screen = "androidx.core:core-splashscreen:1.0.0"

    // Core
    const val core_kt = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val app_compat = "androidx.appcompat:appcompat:${Versions.app_compat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
    const val legacy_support = "androidx.legacy:legacy-support-v4:${Versions.legacy_support}"

    // Navigation
    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigation_hilt = "androidx.hilt:hilt-navigation-fragment:${Versions.navigation_hilt}"

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin_stdlib}"

    // Coroutines
    const val kotlin_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlin_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Fragment
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment_ktx}"

    // SwipeRefresh
    const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipefresh}"

    // Lifecycle
   // const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
   // const val livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
   // const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycle_savedstate = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.lifecycle}"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // Dagger2
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val dagger_annotation = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val dagger_android = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val dagger_android_annotation = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val dagger_android_support = "com.google.dagger:dagger-android-support:${Versions.dagger}"

    // Hilt
    const val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hilt_compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hilt_android_test = "com.google.dagger:hilt-android-testing:${Versions.hilt}"

    // RxJava3
    const val rxjava3_kotlin = "io.reactivex.rxjava3:rxkotlin:${Versions.rxjava3_kotlin}"

    // Timber
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // Paging
    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val room_paging = "androidx.room:room-paging:${Versions.room_paging}"

    // DataStore
    const val datastore_prefs = "androidx.datastore:datastore-preferences:${Versions.datastore_prefs}"

    // Room
    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    // JDK Core Lib Desugar
    const val jdk_desugar_lib = "com.android.tools:desugar_jdk_libs:1.1.5"

    // Square
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val okhttp_urlconnection = "com.squareup.okhttp3:okhttp-urlconnection:${Versions.okhttp}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttp_tls = "com.squareup.okhttp3:okhttp-tls:${Versions.okhttp_tls}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val okhttp_logging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val moshi_converter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val moshi_code_gen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val leak_canary = "com.squareup.leakcanary:leakcanary-android:${Versions.leak_canary}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_kapt = "com.github.bumptech.glide:compiler:${Versions.glide_kapt}"

    // Threeten backport
    const val threetenbp = "com.jakewharton.threetenabp:threetenabp:${Versions.threetenbp}"

    // Compose
    const val compose_ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val compose_material = "androidx.compose.material:material:${Versions.compose}"
    const val compose_tooling_preview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val compose_navigation = "androidx.navigation:navigation-compose:${Versions.compose_navigation}"
    const val compose_activity = "androidx.activity:activity-compose:${Versions.compose_activity}"
    const val compose_paging = "androidx.paging:paging-compose:${Versions.compose_paging}"
    const val compose_hilt_navigation = "androidx.hilt:hilt-navigation-compose:${Versions.compose_hilt_navigation}"
    const val compose_coil_kt = "io.coil-kt:coil-compose:${Versions.compose_coil}"
    const val compose_foundation = "androidx.compose.foundation:foundation:${Versions.compose}"

    // Accompanist
    const val accompanist_systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist_sysui}"
    const val accompanist_swipe_refresh = "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist_swiperefresh}"
    const val accompanist_pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist_pager}"
    const val accompanist_pager_indicator = "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist_pager}"

    // Toggle
    const val toggle = "com.github.angads25:toggle:${Versions.toggle}"
}

object TestDependencies {
    const val test_runner = "androidx.test:runner:${Versions.runner}"
    const val test_rule = "androidx.test:rules:${Versions.runner}"
    const val junit = "junit:junit:${Versions.junit}"
    const val test_ext = "androidx.test.ext:junit:${Versions.test_ext}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    const val coroutines_ktx = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_test}"
    const val core_testing = "android.arch.core:core-testing:${Versions.core_testing}"
    const val mockito_kotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.mockito_kotlin}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val compose_ui_junit = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    const val compose_tooling_test ="androidx.compose.ui:ui-tooling:${Versions.compose}"
}