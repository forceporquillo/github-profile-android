package dev.forcecodes.hov.domain.source

import androidx.paging.ExperimentalPagingApi
import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.data.api.GithubRemoteDataSource
import dev.forcecodes.hov.data.api.models.Owner
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.api.models.StarredReposEntity
import dev.forcecodes.hov.data.cache.KeyIndexDao
import dev.forcecodes.hov.data.cache.LocalUserDataSource
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.domain.di.DETAILS_REPOS
import dev.forcecodes.hov.domain.di.STARRED_REPOS
import dev.forcecodes.hov.domain.di.USER_ORGS
import dev.forcecodes.hov.domain.mapper.OrganizationEntityMapper
import dev.forcecodes.hov.domain.mapper.StarredReposEntityEntityMapper
import dev.forcecodes.hov.domain.mapper.UserDetailsEntityMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

data class BasicInfo(
    val id: Int,
    val name: String
)

interface DetailsRepository {
    fun getUserDetails(
        details: BasicInfo
    ): Flow<Result<UserDetailsEntity>>
    fun searchUser(name: String): Flow<Result<List<UserDetailsEntity>>>

    fun getRepositories(name: String): Flow<Result<List<RepositoryEntity>>>
    fun getStarredRepositories(name: String): Flow<Result<List<StarredReposEntity>>>
    fun getOrganizations(name: String): Flow<Result<List<OrganizationsEntity>>>
}

@OptIn(ExperimentalPagingApi::class)
class DetailsRepositoryImpl @Inject constructor(
    private val organizationEntityMapper: OrganizationEntityMapper,
    private val starredReposEntityEntityMapper: StarredReposEntityEntityMapper,
    private val keyIndexDao: KeyIndexDao,
    private val userLocalDataSource: LocalUserDataSource,
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val entityMapper: UserDetailsEntityMapper,

    @Named(DETAILS_REPOS)
    private val reposPaginator: UserDetailsKeyIndexPaginator,
    @Named(STARRED_REPOS)
    private val starredReposPaginator: UserDetailsKeyIndexPaginator,
    @Named(USER_ORGS)
    private val orgsPaginator: UserDetailsKeyIndexPaginator,
) : NetworkBoundResource(), DetailsRepository {

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

    override fun getRepositories(name: String): Flow<Result<List<RepositoryEntity>>> {
        return reposPaginator.requestData(
            name,
            cacheSource = {
                userLocalDataSource.getUserRepositoriesFlow(name)
            },
            remoteSource = { page ->
                githubRemoteDataSource.getRepositories1(name, page, 100)
            },
            saveFetchResult = {
                userLocalDataSource.saveUserRepositories(it)
            }
        )
    }

    override fun searchUser(name: String): Flow<Result<List<UserDetailsEntity>>> {
        return conflateResource(
            cacheSource = { userLocalDataSource.getUserDetailsFlow(name) },
            remoteSource = { githubRemoteDataSource.getDetails(name) },
            saveFetchResult = { data ->
                userLocalDataSource.saveUserDetails(entityMapper.invoke(data))
            },
            shouldFetch = { cache ->
                // fetch only when db cache is empty or we
                // forcibly invoked to invalidate
                cache.isEmpty()
            }
        )
    }

    override fun getStarredRepositories(name: String): Flow<Result<List<StarredReposEntity>>> {
        return orgsPaginator.requestData(
            name,
            cacheSource = {
                userLocalDataSource.getStarredRepositoresFlow(name)
            },
            remoteSource = {
                githubRemoteDataSource.getStarredRepositories1(name, it, 100)
            },
            saveFetchResult = {
                val entity = it.map { starred ->
                    starredReposEntityEntityMapper.invoke(starred, name)
                }
                userLocalDataSource.saveStarredRepositores(entity)
            }
        )
    }

    override fun getOrganizations(name: String): Flow<Result<List<OrganizationsEntity>>> {
        return orgsPaginator.requestData(
            name,
            cacheSource = {
                userLocalDataSource.getOrganizationsFlow(name)
            },
            remoteSource = { page ->
                githubRemoteDataSource.getOrganizations(name, page, 100)
            },
            saveFetchResult = {
                val entity = it.map { org ->
                    organizationEntityMapper.invoke(org, Owner(name, "23"))
                }

                userLocalDataSource.saveOrganizations(entity)
            }
        )
    }
}

