package dev.forcecodes.gitprofile.domain.usecase.users

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.UiState
import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.domain.source.UsersRepository
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.annotation.concurrent.Immutable
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
