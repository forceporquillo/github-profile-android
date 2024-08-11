package dev.forcecodes.gitprofile.domain.usecase.details

import dev.forcecodes.gitprofile.core.UiState
import dev.forcecodes.gitprofile.core.foldable
import dev.forcecodes.gitprofile.core.internal.Logger
import dev.forcecodes.gitprofile.core.model.DetailsUiModel
import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.domain.DataStrategy
import dev.forcecodes.gitprofile.domain.mapper.DetailsUiMapper
import dev.forcecodes.gitprofile.domain.source.DetailsRepository
import dev.forcecodes.gitprofile.domain.usecase.BaseFlowUseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserDetailsUseCase @Inject constructor(
    private val detailsUiMapper: DetailsUiMapper,
    private val detailsRepository: DetailsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseFlowUseCase<GetUserDetailsUseCase.Params, DetailsViewState>(dispatcher) {

    class Params(val id: Int, val name: String, val strategy: DataStrategy) : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<DetailsViewState> {
        val strategy = parameters.strategy
        return detailsRepository.getUserDetails(parameters.id, parameters.name, strategy)
            .map { result ->
                result.foldable(
                    {
                        DetailsViewState(
                            data = null,
                            isLoading = true,
                            error = null,
                            isForceRefresh = strategy is DataStrategy.RemoteOverCache
                        )
                    },
                    { entity ->
                        DetailsViewState(
                            data = detailsUiMapper.invoke(entity),
                            isLoading = false,
                            error = null,
                            isForceRefresh = strategy is DataStrategy.RemoteOverCache
                        )
                    },
                    { exception ->
                        Logger.e("Error: $exception")
                        DetailsViewState(
                            data = null,
                            isLoading = false,
                            error = exception.message,
                            isForceRefresh = strategy is DataStrategy.RemoteOverCache
                        )
                    }
                )
        }
    }

    override fun mapExceptionToResult(params: Throwable): DetailsViewState {
        return DetailsViewState(false, params.message, null)
    }
}

data class DetailsViewState(
    val isLoading: Boolean,
    val error: String?,
    val data: DetailsUiModel?,
    val isForceRefresh: Boolean = false
) : UiState {

    companion object {
        fun initial() = DetailsViewState(
            data = null,
            isLoading = true,
            error = null
        )
    }
}