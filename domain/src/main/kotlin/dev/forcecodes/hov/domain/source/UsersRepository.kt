package dev.forcecodes.hov.domain.source

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.api.GithubRemoteDataSource
import dev.forcecodes.hov.data.api.conflateResource
import dev.forcecodes.hov.data.cache.LocalUserDataSource
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.domain.mapper.UserEntityMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface UsersRepository {
    fun refreshUser(page: Int): Flow<Result<List<UserEntity>>>
    fun loadMore(since: Int): Flow<Result<List<UserEntity>>>
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: LocalUserDataSource,
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val userEntityMapper: UserEntityMapper
) : UsersRepository {

    override fun refreshUser(page: Int): Flow<Result<List<UserEntity>>> {
        // always refresh from start and
        // consume it so it'll invoke a remote API request.
        return loadFromResource(page, MAX_SIZE)
    }

    override fun loadMore(since: Int): Flow<Result<List<UserEntity>>> {
        return loadFromResource(since, DEFAULT_PAGE_SIZE)
    }

    private fun loadFromResource(
        since: Int,
        maxSize: Int,
        invalidate: Boolean = true,
    ): Flow<Result<List<UserEntity>>> = conflateResource(
        cacheSource = { userLocalDataSource.getUserFlow() },
        remoteSource = { githubRemoteDataSource.getUsers(since, maxSize) },
        saveFetchResult = { list -> userLocalDataSource.saveUsers(userEntityMapper.invoke(list)) },
        shouldFetch = { cache ->
            // fetch only when db cache is empty or we
            // forcibly invoked to invalidate
            cache.isEmpty() || invalidate
        }
    )

    companion object {
        private const val DEFAULT_PAGE_SIZE = 30
        private const val MAX_SIZE = 100
    }
}