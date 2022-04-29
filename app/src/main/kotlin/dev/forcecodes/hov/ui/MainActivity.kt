package dev.forcecodes.hov.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.hov.R
import dev.forcecodes.hov.binding.viewBinding
import dev.forcecodes.hov.data.theme.Theme
import dev.forcecodes.hov.databinding.ActivityMainBinding
import dev.forcecodes.hov.extensions.updateForTheme
import dev.forcecodes.hov.theme.ThemeViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnRefreshListener {

    private val viewModel by viewModels<MainActivityViewModel>()
    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateForTheme(themeViewModel.currentTheme)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                themeViewModel.theme.collect {
                    updateForTheme(it)
                    binding.toggle.isOn = it == Theme.LIGHT
                }
            }
        }

        binding.toggle.setOnToggledListener { _, isOn ->
            themeViewModel.setTheme(if (isOn) Theme.LIGHT else Theme.DARK)
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

    // unused
    override fun onRefresh(page: Int) {
        viewModel.onRefresh(page)
    }

    private fun isInDarkMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}