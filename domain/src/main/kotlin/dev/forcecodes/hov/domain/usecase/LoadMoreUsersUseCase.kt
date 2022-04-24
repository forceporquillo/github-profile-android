package dev.forcecodes.hov.domain.usecase

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.UiState
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.domain.source.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadMoreUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : LoadResourceUseCase<LoadMoreUsersUseCase.Params>(ioDispatcher) {

    class Params(val since: Int) : UseCaseParams.Params()

    override fun fromSource(params: Params): Flow<Result<List<UserEntity>>> {
        return usersRepository.loadMore(params.since)
    }
}

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

data class ListItemUiState(
    val hasItems: Boolean,
    val isLoading: Boolean,
    val error: String? = null
) : UiState {

    companion object {
        fun initial() = ListItemUiState(
            hasItems = false,
            isLoading = true,
            error = null,
        )
    }
}
