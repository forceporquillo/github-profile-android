package dev.forcecodes.hov.domain.usecase

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.domain.source.UsersRepository
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
        // loads 100 github users per page refresh request
        return usersRepository.refreshUser(params.page)
    }
}
