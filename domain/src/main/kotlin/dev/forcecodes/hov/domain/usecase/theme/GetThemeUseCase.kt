package dev.forcecodes.hov.domain.usecase.theme

import android.os.Build
import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.data.prefs.PreferenceStorage
import dev.forcecodes.hov.data.theme.Theme
import dev.forcecodes.hov.data.theme.themeFromStorageKey
import dev.forcecodes.hov.domain.usecase.FlowUseCase
import dev.forcecodes.hov.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<GetThemeUseCase.Params, Theme>(dispatcher) {

    class Params : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<Result<Theme>> {
        val selectedTheme = preferenceStorage.selectedTheme
        return selectedTheme.map {
            val theme = themeFromStorageKey(it)
                ?: when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Theme.SYSTEM
                    else -> Theme.BATTERY_SAVER
                }

            Result.Success(theme)
        }
    }
}