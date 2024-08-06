package dev.forcecodes.gitprofile.domain.usecase.users

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.foldable
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
            result.foldable(
                onLoading = {
                    ListItemUiState(
                        hasItems = false,
                        isLoading = true,
                    )
                },
                onSuccess = {
                    ListItemUiState(
                        hasItems = it?.isNotEmpty() == true,
                        isLoading = false,
                    )
                },
                onFailure = {
                    ListItemUiState(
                        hasItems = false,
                        isLoading = true,
                    )
                }
            )
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