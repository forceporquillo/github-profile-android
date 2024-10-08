package dev.forcecodes.gitprofile.data.cache

import dev.forcecodes.gitprofile.core.qualifiers.IoDispatcher
import dev.forcecodes.gitprofile.data.api.models.RepositoryEntity
import dev.forcecodes.gitprofile.data.api.models.StarredReposEntity
import dev.forcecodes.gitprofile.data.cache.entity.OrganizationsEntity
import dev.forcecodes.gitprofile.data.cache.entity.UserDetailsEntity
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity
import dev.forcecodes.gitprofile.data.internal.InternalApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface LocalUserDataSource {

    suspend fun saveUsers(users: List<UserEntity>)
    fun getUserFlow(): Flow<List<UserEntity>>

    suspend fun saveUserDetails(detailsEntity: UserDetailsEntity)
    fun getUserDetailsFlow(id: Int): Flow<UserDetailsEntity>

    fun getUserDetailsFlow(name: String): Flow<List<UserDetailsEntity>>

    suspend fun getUserDetails(name: String): UserDetailsEntity

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

    override suspend fun saveUsers(users: List<UserEntity>) {
        withContext(dispatcher) {
            appDatabase.userDao()
                .saveUsers(users)
        }
    }

    override fun getUserFlow(): Flow<List<UserEntity>> {
        return appDatabase.userDao()
            .getUsers()
    }

    override suspend fun saveUserDetails(detailsEntity: UserDetailsEntity) {
        withContext(dispatcher) {
            appDatabase.userDetails()
                .saveDetails(detailsEntity)
        }
    }

    override fun getUserDetailsFlow(id: Int): Flow<UserDetailsEntity> {
        return appDatabase.userDetails()
            .getDetails(id)
    }

    override fun getUserDetailsFlow(name: String): Flow<List<UserDetailsEntity>> {
        return appDatabase.userDetails()
            .getDetails(name.plus("%"))
    }

    override suspend fun getUserDetails(name: String): UserDetailsEntity {
        return withContext(dispatcher) {
            appDatabase.userDetails()
                .getUserDetails(name)
        }
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
        withContext(dispatcher) {
            provider.runAsTransaction {
                appDatabase.organizationsDao()
                    .saveAll(orgs)
            }
        }
    }

    override fun getUserRepositoriesFlow(name: String): Flow<List<RepositoryEntity>> {
        return appDatabase.userRepositories().getReposFlow(name)
    }

    override suspend fun saveStarredRepositores(repos: List<StarredReposEntity>) {
        withContext(dispatcher) {
            provider.runAsTransaction {
                appDatabase.starredReposDao()
                    .saveAll(repos)
            }
        }
    }

    override fun getStarredRepositoresFlow(name: String): Flow<List<StarredReposEntity>> {
        return appDatabase.starredReposDao().getStarredRepos(name)
    }

}