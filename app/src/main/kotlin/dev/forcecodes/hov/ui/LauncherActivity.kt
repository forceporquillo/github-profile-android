package dev.forcecodes.hov.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.extensions.newThemedIntent
import dev.forcecodes.hov.extensions.repeatOnLifecycle
import dev.forcecodes.hov.theme.ThemeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

    private fun navigateToMainActivity() {
        val theme = themeViewModel.currentTheme
        startActivity(newThemedIntent(theme, MainActivity::class))
        overridePendingTransition(0, 0);
        finish()
    }
}