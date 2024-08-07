package dev.forcecodes.android.gitprofile.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.android.gitprofile.R
import dev.forcecodes.android.gitprofile.binding.viewBinding
import dev.forcecodes.gitprofile.data.theme.Theme
import dev.forcecodes.android.gitprofile.databinding.ActivityMainBinding
import dev.forcecodes.android.gitprofile.extensions.doOnApplyWindowInsets
import dev.forcecodes.android.gitprofile.extensions.repeatOnLifecycle
import dev.forcecodes.android.gitprofile.extensions.updateForTheme
import dev.forcecodes.android.gitprofile.theme.ThemeViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()
    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        currentTheme?.let {
            if (savedInstanceState == null) {
                updateForTheme(it)
            } else {
                if (themeViewModel.currentTheme == it) {
                    updateForTheme(it)
                }
            }
        }

        setContentView(binding.root)

        binding.root.doOnApplyWindowInsets { view, windowInsetsCompat, paddingState ->
            view.doOnLayout {
                val systemBars = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updatePadding(top = systemBars.top + paddingState.top)
            }
        }

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        repeatOnLifecycle {
            themeViewModel.theme
                .onEach {
                    binding.toggle.isOn = it == Theme.LIGHT || !isInDarkMode()
                }
                .debounce(500L)
                .collect {
                    updateForTheme(it)
                }
        }

        binding.toggle.setOnToggledListener { v, isOn ->
            themeViewModel.setTheme(isOn)
            v.isOn = !isOn
        }

        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_controller_view) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        if (isInDarkMode()) {
            binding.bottomNav.elevation = 0F
        }

        navController.addOnDestinationChangedListener { _, _, _ ->

            val uiState = viewModel.state.value

            if (uiState.isLoading) {
                return@addOnDestinationChangedListener
            }

            // refresh everytime we change nav destinations.
            if (!uiState.hasItems) {
                viewModel.onRetry()
            }
        }
    }

    private fun isInDarkMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private val currentTheme: Theme?
        get() {
            @Suppress("deprecation")
            return intent.extras?.getSerializable("theme") as? Theme
        }

}