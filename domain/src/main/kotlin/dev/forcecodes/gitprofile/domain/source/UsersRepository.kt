package dev.forcecodes.gitprofile.domain.source

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.data.api.GithubRemoteDataSource
import dev.forcecodes.gitprofile.data.cache.LocalUserDataSource
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.domain.mapper.UserEntityMapper
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
) : NetworkBoundResource(), UsersRepository {

    override fun refreshUser(page: Int): Flow<Result<List<UserEntity>>> {
        // always refresh from start and
        // consume it so it'll invoke a remote API request.
        return conflateResource(
            cacheSource = { userLocalDataSource.getUserFlow() },
            remoteSource = { githubRemoteDataSource.getUsers(page, MAX_SIZE) },
            accumulator = { userLocalDataSource.saveUsers(userEntityMapper.invoke(it))},
            shouldFetch = { cache ->
                cache?.isEmpty() == true
            },
            strategy = FailureStrategy.ThrowOnFailure,
            fetchBehavior = FetchBehavior.FetchWithProgress
        )
    }

    override fun loadMore(since: Int): Flow<Result<List<UserEntity>>> {
        return conflateResource(
            accumulator = { userLocalDataSource.saveUsers(userEntityMapper.invoke(it)) },
            remoteSource = { githubRemoteDataSource.getUsers(since, DEFAULT_PAGE_SIZE) },
            cacheSource = { userLocalDataSource.getUserFlow() },
            shouldFetch = { cache ->
                cache?.isEmpty() == true
            }
        )
    }
}

private const val DEFAULT_PAGE_SIZE = 30
private const val MAX_SIZE = 100