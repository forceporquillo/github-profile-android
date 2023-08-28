@file:Suppress("unused")

package dev.forcecodes.hov.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.hov.core.data
import dev.forcecodes.hov.data.theme.Theme
import dev.forcecodes.hov.domain.usecase.theme.GetAvailableThemesUseCase
import dev.forcecodes.hov.domain.usecase.theme.SetThemeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Thin ViewModel for themed Activities that don't have another ViewModel to use with
 * [ThemedActivityDelegate].
 */

@HiltViewModel
class ThemeViewModel @Inject constructor(
    themedActivityDelegate: ThemedActivityDelegate,
    private val getAvailableThemesUseCase: GetAvailableThemesUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate {

    val availableThemes: StateFlow<List<Theme>> = flow {
        emit(getAvailableThemesUseCase(GetAvailableThemesUseCase.Params()).data ?: listOf())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    private var themeChangeJob: Job? = null

    fun setTheme(isLight: Boolean) {
        themeChangeJob = viewModelScope.launch {
            val theme = if (isLight) Theme.LIGHT else Theme.DARK
            setThemeUseCase.invoke(SetThemeUseCase.Params(theme))
        }
    }
}