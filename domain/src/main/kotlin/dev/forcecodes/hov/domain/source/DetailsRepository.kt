package dev.forcecodes.hov.domain.source

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.data.api.GithubRemoteDataSource
import dev.forcecodes.hov.data.cache.LocalUserDataSource
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.domain.mapper.UserDetailsEntityMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DetailsRepository {
    fun getUserDetails(details: BasicInfo): Flow<Result<UserDetailsEntity>>
}

class DetailsRepositoryImpl @Inject constructor(
    private val userLocalDataSource: LocalUserDataSource,
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val entityMapper: UserDetailsEntityMapper,
) : DetailsRepository {

    override fun getUserDetails(details: BasicInfo): Flow<Result<UserDetailsEntity>> {
        return conflateResource(
            cacheSource = {
                userLocalDataSource.getUserDetailsFlow(details.id)
            },
            remoteSource = {
                githubRemoteDataSource.getDetails(details.name)
            },
            saveFetchResult = {
                userLocalDataSource.saveUserDetails(entityMapper.invoke(it))
            },
            shouldFetch = { true }
        )
    }
}

data class BasicInfo(
    val id: Int,
    val name: String
)