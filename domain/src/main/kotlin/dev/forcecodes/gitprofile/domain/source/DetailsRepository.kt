package dev.forcecodes.gitprofile.domain.source

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.data.api.GithubRemoteDataSource
import dev.forcecodes.gitprofile.data.api.models.Owner
import dev.forcecodes.gitprofile.data.api.models.RepositoryEntity
import dev.forcecodes.gitprofile.data.api.models.StarredReposEntity
import dev.forcecodes.gitprofile.data.cache.LocalUserDataSource
import dev.forcecodes.gitprofile.data.cache.entity.OrganizationsEntity
import dev.forcecodes.gitprofile.data.cache.entity.UserDetailsEntity
import dev.forcecodes.gitprofile.domain.di.DETAILS_REPOS
import dev.forcecodes.gitprofile.domain.di.STARRED_REPOS
import dev.forcecodes.gitprofile.domain.di.USER_ORGS
import dev.forcecodes.gitprofile.domain.mapper.OrganizationEntityMapper
import dev.forcecodes.gitprofile.domain.mapper.StarredReposEntityMapper
import dev.forcecodes.gitprofile.domain.mapper.UserDetailsEntityMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

data class BasicInfo(
    val id: Int,
    val name: String
)

interface DetailsRepository {
    fun searchUser(name: String): Flow<Result<List<UserDetailsEntity>>>
    fun getUserDetails(details: BasicInfo): Flow<Result<UserDetailsEntity>>
    fun getRepositories(name: String): Flow<Result<List<RepositoryEntity>>>
    fun getStarredRepositories(name: String): Flow<Result<List<StarredReposEntity>>>
    fun getOrganizations(name: String): Flow<Result<List<OrganizationsEntity>>>
}

class DetailsRepositoryImpl @Inject constructor(
    private val organizationEntityMapper: OrganizationEntityMapper,
    private val starredReposEntity: StarredReposEntityMapper,
    private val userLocalDataSource: LocalUserDataSource,
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val entityMapper: UserDetailsEntityMapper,
    @Named(DETAILS_REPOS) private val detailsPagination: UserDetailsKeyIndexPagination,
    @Named(STARRED_REPOS) private val startedReposPagination: UserDetailsKeyIndexPagination,
    @Named(USER_ORGS) private val organizationsPagination: UserDetailsKeyIndexPagination,
) : NetworkBoundResource(), DetailsRepository {

    override fun getUserDetails(details: BasicInfo): Flow<Result<UserDetailsEntity>> {
        return conflateResource(
            cacheSource = { userLocalDataSource.getUserDetailsFlow(details.id) },
            remoteSource = { githubRemoteDataSource.getDetails(details.name) },
            accumulator = { data -> userLocalDataSource.saveUserDetails(entityMapper.invoke(data)) },
            shouldFetch = { cache -> cache == null },
            strategy = FailureStrategy.ThrowOnFailure
        )
    }

    override fun searchUser(name: String): Flow<Result<List<UserDetailsEntity>>> {
        return conflateResource(
            cacheSource = { userLocalDataSource.getUserDetailsFlow(name) },
            remoteSource = { githubRemoteDataSource.getDetails(name) },
            accumulator = { data -> userLocalDataSource.saveUserDetails(entityMapper.invoke(data)) },
            shouldFetch = { cache ->
                // fetch only when db cache is empty or we
                // forcibly invoked to invalidate
                cache?.isEmpty() == true
            },
            fetchBehavior = FetchBehavior.FetchSilently,
            strategy = FailureStrategy.ThrowOnFailure
        )
    }

    override fun getRepositories(name: String): Flow<Result<List<RepositoryEntity>>> {
        return detailsPagination.requestData(
            userName = name,
            cacheSource = { userLocalDataSource.getUserRepositoriesFlow(name) },
            remoteSource = { page -> githubRemoteDataSource.getRepositories(name, page, MAX_FETCH_SIZE) },
            saveFetchResult = { ownRepos -> userLocalDataSource.saveUserRepositories(ownRepos) }
        )
    }

    override fun getStarredRepositories(name: String): Flow<Result<List<StarredReposEntity>>> {
        return startedReposPagination.requestData(
            userName = name,
            cacheSource = { userLocalDataSource.getStarredRepositoresFlow(name) },
            remoteSource = { page -> githubRemoteDataSource.getStarredRepositories(name, page, MAX_FETCH_SIZE) },
            saveFetchResult = { starredRepos ->
                val mappedStarredRepos = starredRepos.map { repo ->
                    starredReposEntity.invoke(repo, name)
                }
                userLocalDataSource.saveStarredRepositores(mappedStarredRepos)
            }
        )
    }

    override fun getOrganizations(name: String): Flow<Result<List<OrganizationsEntity>>> {
        return organizationsPagination.requestData(
            userName = name,
            cacheSource = { userLocalDataSource.getOrganizationsFlow(name) },
            remoteSource = { page -> githubRemoteDataSource.getOrganizations(name, page, MAX_FETCH_SIZE) },
            saveFetchResult = { orgs ->
                if (orgs.isNotEmpty()) {
                    val details = userLocalDataSource.getUserDetails(name)
                    val mappedOrgs = orgs.map { org ->
                        organizationEntityMapper.invoke(org, Owner(name, details.id.toString()))
                    }
                    userLocalDataSource.saveOrganizations(mappedOrgs)
                }
            }
        )
    }
}

private const val MAX_FETCH_SIZE = 20