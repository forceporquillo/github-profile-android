package dev.forcecodes.hov.data.cache

import dev.forcecodes.hov.core.qualifiers.IoDispatcher
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.api.models.StarredReposEntity
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.data.internal.InternalApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface LocalUserDataSource {

    fun saveUsers(users: List<UserEntity>)
    fun getUserFlow(): Flow<List<UserEntity>>

    fun saveUserDetails(detailsEntity: UserDetailsEntity)
    fun getUserDetailsFlow(id: Int): Flow<UserDetailsEntity>

    suspend fun saveUserRepositories(repos: List<RepositoryEntity>)
    fun getUserRepositoriesFlow(name: String): Flow<List<RepositoryEntity>>

    suspend fun saveOrganizations(orgs: List<OrganizationsEntity>)
    fun getOrganizationsFlow(name: String): Flow<List<OrganizationsEntity>>

    suspend fun saveStarredRepositores(repos: List<StarredReposEntity>)
    fun getStarredRepositoresFlow(name: String): Flow<List<StarredReposEntity>>
}

@Singleton
class LocalDataSourceImpl @Inject constructor(
    @InternalApi private val appDatabase: AppDatabase,
    private val provider: TransactionProvider,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LocalUserDataSource {

    override fun saveUsers(users: List<UserEntity>) {
        appDatabase.userDao()
            .saveUsers(users)
    }

    override fun getUserFlow(): Flow<List<UserEntity>> {
        return appDatabase.userDao()
            .getUsers()
    }

    override fun saveUserDetails(detailsEntity: UserDetailsEntity) {
        appDatabase.userDetails()
            .saveDetails(detailsEntity)
    }

    override fun getUserDetailsFlow(id: Int): Flow<UserDetailsEntity> {
        return appDatabase.userDetails()
            .getDetails(id)
    }

    override fun getOrganizationsFlow(name: String): Flow<List<OrganizationsEntity>> {
        return appDatabase.organizationsDao().getOrganizations(name)
    }

    override suspend fun saveUserRepositories(repos: List<RepositoryEntity>) {
        withContext(dispatcher) {
            provider.runAsTransaction {
                appDatabase.userRepositories()
                    .saveAll(repos)
            }
        }
    }

    override suspend fun saveOrganizations(orgs: List<OrganizationsEntity>) {
        provider.runAsTransaction {
            appDatabase.organizationsDao()
                .saveAll(orgs)
        }
    }

    override fun getUserRepositoriesFlow(name: String): Flow<List<RepositoryEntity>> {
        return appDatabase.userRepositories().getReposFlow(name)
    }

    override suspend fun saveStarredRepositores(repos: List<StarredReposEntity>) {
        provider.runAsTransaction {
            appDatabase.starredReposDao()
                .saveAll(repos)
        }
    }

    override fun getStarredRepositoresFlow(name: String): Flow<List<StarredReposEntity>> {
        return appDatabase.starredReposDao().getStarredRepos(name)
    }

}