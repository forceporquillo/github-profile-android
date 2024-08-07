package dev.forcecodes.android.gitprofile.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.android.gitprofile.extensions.newThemedIntent
import dev.forcecodes.android.gitprofile.extensions.repeatOnLifecycle
import dev.forcecodes.android.gitprofile.theme.ThemeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var keepScreenOn = true
        splashScreen.setKeepOnScreenCondition {
            keepScreenOn
        }

        repeatOnLifecycle {
            themeViewModel.theme.onEach {
                keepScreenOn = false
            }.collect()
        }

        splashScreen.setOnExitAnimationListener { splashProvider ->
            splashProvider.remove()
            navigateToMainActivity()
        }
    }

    @Suppress("DEPRECATION")
    private fun navigateToMainActivity() {
        val theme = themeViewModel.currentTheme
        startActivity(newThemedIntent(theme, MainActivity::class))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
        finish()
    }
}