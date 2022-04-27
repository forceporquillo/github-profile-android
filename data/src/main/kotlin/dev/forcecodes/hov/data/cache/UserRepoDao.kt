package dev.forcecodes.hov.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.hov.data.api.models.RepositoryEntity

@Dao
interface UserRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(repos: List<RepositoryEntity>)

    @Query("SELECT * FROM repositoryentity WHERE owner_login=:name ORDER BY stargazersCount DESC")
    fun getRepos(name: String): PagingSource<Int, RepositoryEntity>

    @Query("DELETE FROM RepositoryEntity WHERE owner_login=:name")
    suspend fun deleteAll(name: String)
}