package dev.forcecodes.hov.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.hov.data.api.models.StarredReposEntity
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(orgs: List<OrganizationsEntity>)

    @Query("DELETE FROM organizationsentity WHERE owner_login=:name")
    suspend fun deleteAll(name: String)

    @Query("SELECT * FROM organizationsentity WHERE owner_login=:name")
    fun getOrganizations(name: String): Flow<List<OrganizationsEntity>>
}

@Dao
interface StarredReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(repos: List<StarredReposEntity>)

    @Query("DELETE FROM starredreposentity WHERE starred_owner=:name")
    suspend fun deleteAll(name: String)

    @Query("SELECT * FROM starredreposentity WHERE starred_owner=:name")
    fun getStarredRepos(name: String): Flow<List<StarredReposEntity>>
}
