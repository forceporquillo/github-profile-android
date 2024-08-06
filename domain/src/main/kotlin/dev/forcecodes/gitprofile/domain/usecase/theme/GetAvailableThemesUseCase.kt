package dev.forcecodes.gitprofile.domain.usecase.theme

import androidx.core.os.BuildCompat
import dev.forcecodes.gitprofile.core.qualifiers.MainImmediateDispatcher
import dev.forcecodes.gitprofile.data.theme.Theme
import dev.forcecodes.gitprofile.domain.usecase.UseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetAvailableThemesUseCase @Inject constructor(
    @MainImmediateDispatcher dispatcher: CoroutineDispatcher
) : UseCase<GetAvailableThemesUseCase.Params, List<Theme>>(dispatcher) {

    class Params : UseCaseParams.Params()

    @Suppress("DEPRECATION")
    override suspend fun execute(parameters: Params): List<Theme> = when {
        BuildCompat.isAtLeastQ() -> {
            listOf(Theme.LIGHT, Theme.DARK, Theme.SYSTEM)
        }
        else -> {
            listOf(Theme.LIGHT, Theme.DARK, Theme.BATTERY_SAVER)
        }
    }
}