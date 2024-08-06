package dev.forcecodes.gitprofile.domain.usecase.users

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.domain.usecase.BaseFlowUseCase
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class LoadResourceUseCase<in T : UseCaseParams.Params>(
    coroutineDispatcher: CoroutineDispatcher
) : BaseFlowUseCase<T, ListItemUiState>(coroutineDispatcher) {

    override fun execute(parameters: T): Flow<ListItemUiState> {
        return fromSource(parameters).map { result ->
            when (result) {
                is Result.Success -> {
                    ListItemUiState(
                        hasItems = result.data.isNotEmpty(),
                        isLoading = false,
                    )
                }
                is Result.Error -> {
                    ListItemUiState(
                        hasItems = result.data?.isNotEmpty() ?: false,
                        isLoading = false,
                        error = result.exception.message,
                    )
                }
                is Result.Loading -> {
                    ListItemUiState(
                        hasItems = result.data?.isNotEmpty() ?: false,
                        isLoading = true,
                    )
                }
            }
        }
    }

    override fun mapExceptionToResult(params: Throwable): ListItemUiState {
        return ListItemUiState(
            error = params.message,
            hasItems = false,
            isLoading = false
        )
    }

    protected abstract fun fromSource(params: T): Flow<Result<List<UserEntity>>>
}