package dev.forcecodes.hov.domain.source

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.data.api.ApiResponse
import dev.forcecodes.hov.data.api.GithubRemoteDataSource
import dev.forcecodes.hov.data.cache.LocalUserDataSource
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.domain.mapper.UserDetailsEntityMapper
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
    private val userEntityMapper: UserEntityMapper,
    private val detailsEntityMapper: UserDetailsEntityMapper
) : NetworkBoundResource(), UsersRepository {

    override fun refreshUser(page: Int): Flow<Result<List<UserEntity>>> {
        // always refresh from start and
        // consume it so it'll invoke a remote API request.
        return loadFromResource(
            remoteDataSource = {
                githubRemoteDataSource.getUsers(page, MAX_SIZE)
            },
            localDataSource = {
                userLocalDataSource.saveUsers(userEntityMapper.invoke(it))
            })
    }

    override fun loadMore(since: Int): Flow<Result<List<UserEntity>>> {
        return loadFromResource(
            remoteDataSource = {
                githubRemoteDataSource.getUsers(since, DEFAULT_PAGE_SIZE)
            },
            localDataSource = {
                userLocalDataSource.saveUsers(userEntityMapper.invoke(it))
            })
    }

    private fun <T> loadFromResource(
        invalidate: Boolean = true,
        remoteDataSource: suspend () -> ApiResponse<T>,
        localDataSource: suspend (T) -> Unit
    ): Flow<Result<List<UserEntity>>> = conflateResource(
        cacheSource = { userLocalDataSource.getUserFlow() },
        remoteSource = { remoteDataSource() },
        accumulator = { data -> localDataSource(data) },
        shouldFetch = { cache ->
            // fetch only when db cache is empty or we
            // forcibly invoked to invalidate
            cache?.isEmpty() == true || invalidate
        }
    )

    companion object {
        private const val DEFAULT_PAGE_SIZE = 30
        private const val MAX_SIZE = 100
    }
}