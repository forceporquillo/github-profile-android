package dev.forcecodes.gitprofile.domain.usecase.theme

import android.os.Build
import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.qualifiers.DefaultDispatcher
import dev.forcecodes.gitprofile.data.prefs.PreferenceStorage
import dev.forcecodes.gitprofile.data.theme.Theme
import dev.forcecodes.gitprofile.data.theme.themeFromStorageKey
import dev.forcecodes.gitprofile.domain.usecase.FlowUseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class ObserveThemeModeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @DefaultDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<ObserveThemeModeUseCase.Params, Theme>(dispatcher) {

    class Params : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<Result<Theme>> {
        return preferenceStorage.selectedTheme.map {
            val theme = themeFromStorageKey(it)
                ?: when {
                    Build.VERSION.SDK_INT >= 29 -> Theme.SYSTEM
                    else -> Theme.BATTERY_SAVER
                }
            Result.Success(theme)
        }
    }
}