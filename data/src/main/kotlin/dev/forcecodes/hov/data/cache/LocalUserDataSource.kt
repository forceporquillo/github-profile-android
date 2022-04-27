package dev.forcecodes.hov.data.cache

import androidx.paging.PagingSource
import androidx.room.withTransaction
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.cache.entity.KeyIndex
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.data.cache.entity.UserEntity
import dev.forcecodes.hov.data.internal.InternalApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface LocalUserDataSource {
    fun saveUsers(users: List<UserEntity>)
    fun getUserFlow(): Flow<List<UserEntity>>

    fun saveUserDetails(detailsEntity: UserDetailsEntity)
    fun getUserDetailsFlow(id: Int): Flow<UserDetailsEntity>

    suspend fun saveRepositories(repos: List<RepositoryEntity>)

    fun getRepositories(name: String): PagingSource<Int, RepositoryEntity>
    fun getOrganizations(name: String): PagingSource<Int, OrganizationsEntity>

    suspend fun insertAllReposWithKeys(
        keys: List<KeyIndex>,
        items: List<RepositoryEntity>,
        refresh: Boolean,
        name: String,
        tag: String
    )

    suspend fun insertAllOrgsWithKeys(
        keys: List<KeyIndex>,
        items: List<OrganizationsEntity>,
        clear: Boolean,
        name: String,
        tag: String
    )
}

@Singleton
class LocalDataSourceImpl @Inject constructor(
    @InternalApi private val appDatabase: AppDatabase
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

    override fun getRepositories(name: String): PagingSource<Int, RepositoryEntity> {
        return appDatabase.userRepositories()
            .getRepos(name)
    }

    override suspend fun saveRepositories(repos: List<RepositoryEntity>) {
        appDatabase.withTransaction {
            appDatabase.userRepositories()
                .saveAll(repos)
        }
    }

    override suspend fun insertAllReposWithKeys(
        keys: List<KeyIndex>,
        items: List<RepositoryEntity>,
        refresh: Boolean,
        name: String,
        tag: String
    ) {
        appDatabase.withTransaction {
            if (refresh) {
                appDatabase.keyIndexDao().clearRemoteKeys("$name$tag")
                appDatabase.userRepositories().deleteAll(name)
            }

            appDatabase.keyIndexDao().insertAll(keys)
            appDatabase.userRepositories().saveAll(items)
        }
    }

    override fun getOrganizations(name: String): PagingSource<Int, OrganizationsEntity> {
        return appDatabase.organizationsDao()
            .getOrganizations(name)
    }

    override suspend fun insertAllOrgsWithKeys(
        keys: List<KeyIndex>,
        items: List<OrganizationsEntity>,
        clear: Boolean,
        name: String,
        tag: String
    ) {
        appDatabase.withTransaction {
            if (clear) {
                appDatabase.keyIndexDao().clearRemoteKeys(tag)
                appDatabase.organizationsDao().deleteAll(name)
            }

            appDatabase.keyIndexDao().insertAll(keys)
            appDatabase.organizationsDao().saveAll(items)
        }
    }
}