package dev.forcecodes.hov.domain.usecase.users

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.UiState
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.domain.source.UsersRepository
import dev.forcecodes.hov.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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
