package dev.forcecodes.gitprofile.domain.usecase.theme

import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.data.prefs.PreferenceStorage
import dev.forcecodes.gitprofile.data.theme.Theme
import dev.forcecodes.gitprofile.domain.usecase.UseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class SetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<SetThemeUseCase.Params, Unit>(dispatcher) {

    class Params(val theme: Theme) : UseCaseParams.Params()

    override suspend fun execute(parameters: Params) {
        preferenceStorage.selectTheme(parameters.theme.storageKey)
    }
}