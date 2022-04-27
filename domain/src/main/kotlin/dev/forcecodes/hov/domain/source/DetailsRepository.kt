package dev.forcecodes.hov.domain.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.api.GithubRemoteDataSource
import dev.forcecodes.hov.data.api.models.Owner
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.cache.KeyIndexDao
import dev.forcecodes.hov.data.cache.LocalUserDataSource
import dev.forcecodes.hov.data.cache.entity.KeyIndex
import dev.forcecodes.hov.domain.mapper.OrganizationEntityMapper
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.domain.mapper.UserDetailsEntityMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class BasicInfo(
    val id: Int,
    val name: String
)

interface DetailsRepository {
    fun getUserDetails(details: BasicInfo): Flow<Result<UserDetailsEntity>>
    fun getUserRepositories(name: String, config: PagingConfig): Flow<PagingData<RepositoryEntity>>
    fun getUserOrganizations(
        name: String,
        id: Int,
        config: PagingConfig
    ): Flow<PagingData<OrganizationsEntity>>
}

class DetailsRepositoryImpl @Inject constructor(
    private val organizationEntityMapper: OrganizationEntityMapper,
    private val keyIndexDao: KeyIndexDao,
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

    @OptIn(ExperimentalPagingApi::class)
    override fun getUserRepositories(
        name: String,
        config: PagingConfig
    ): Flow<PagingData<RepositoryEntity>> {
        return Pager(
            config = config,
            remoteMediator = PagedKeyRemoteMediator(
                onSuccess = { page, size ->
                    githubRemoteDataSource.getRepositories(name, page, size)
                },
                refreshKeys = { items, nextKey, prevKey, refresh ->
                    Logger.d("getUserRepositories.keys")
                    val keys = items.map {
                        KeyIndex(
                            it.id,
                            prevSince = prevKey,
                            nextSince = nextKey,
                            tag = "$name:repos"
                        )
                    }
                    userLocalDataSource.insertAllReposWithKeys(
                        keys,
                        items,
                        refresh,
                        name,
                        ":repos"
                    )
                },
                findKeyDelegate = {
                    keyIndexDao.remoteKeysRepoId(it.id, "$name:repos")
                }
            ),
            pagingSourceFactory = { userLocalDataSource.getRepositories(name) }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getUserOrganizations(
        name: String,
        id: Int,
        config: PagingConfig
    ): Flow<PagingData<OrganizationsEntity>> {
        return Pager(
            config = config,
            remoteMediator = PagedKeyRemoteMediator(
                onSuccess = { page, size ->
                    githubRemoteDataSource.getOrganizations(name, page, size)
                },
                refreshKeys = { items, nextKey, prevKey, clear ->

                    Logger.d("getUserOrganizations.keys")
                    val keys = items.map {
                        KeyIndex(
                            it.id,
                            prevSince = prevKey,
                            nextSince = nextKey,
                            tag = "$id$name:orgs"
                        )
                    }
                    val dataEntity = items.map { org ->
                        organizationEntityMapper.invoke(org, Owner(name, id.toString()))
                    }
                    userLocalDataSource.insertAllOrgsWithKeys(
                        keys,
                        dataEntity,
                        clear,
                        name,
                        "$id$name:orgs"
                    )
                },
                findKeyDelegate = {
                    keyIndexDao.remoteKeysRepoId(it.id, "$id$name:orgs")
                }
            ),
            pagingSourceFactory = { userLocalDataSource.getOrganizations(name) }
        ).flow
    }
}

