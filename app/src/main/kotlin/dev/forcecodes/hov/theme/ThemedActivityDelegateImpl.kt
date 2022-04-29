package dev.forcecodes.hov.theme

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.qualifiers.ApplicationScope
import dev.forcecodes.hov.core.successOr
import dev.forcecodes.hov.data.theme.Theme
import dev.forcecodes.hov.domain.usecase.theme.GetThemeUseCase
import dev.forcecodes.hov.domain.usecase.theme.ObserveThemeModeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ThemedActivityDelegateImpl @Inject constructor(
    @ApplicationScope externalScope: CoroutineScope,
    observeThemeUseCase: ObserveThemeModeUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ThemedActivityDelegate {

    override val theme: StateFlow<Theme> =
        observeThemeUseCase(ObserveThemeModeUseCase.Params()).map {
            it.successOr(Theme.SYSTEM)
        }.stateIn(externalScope, SharingStarted.Eagerly, Theme.SYSTEM)

    override val currentTheme: Theme
        get() = runBlocking { // Using runBlocking to execute this coroutine synchronously
            getThemeUseCase(GetThemeUseCase.Params()).map {
                if (it is Result.Success) it.data else Theme.SYSTEM
            }.first()
        }
}