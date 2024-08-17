package dev.forcecodes.gitprofile.domain.usecase.users

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.domain.DataStrategy
import dev.forcecodes.gitprofile.domain.source.UsersRepository
import dev.forcecodes.gitprofile.domain.usecase.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : LoadResourceUseCase<RefreshUsersUseCase.Params>(ioDispatcher) {

    class Params(val page: Int) : UseCaseParams.Params()

    override fun fromSource(params: Params): Flow<Result<List<UserEntity>>> {
        return usersRepository.refreshUser(params.page, DataStrategy.CacheOverRemote)
    }
}
