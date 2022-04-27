package dev.forcecodes.hov.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity


@Dao
interface OrganizationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(repos: List<OrganizationsEntity>)

    @Query("SELECT * FROM organizationsentity WHERE owner_login=:name ORDER BY id ASC")
    fun getOrganizations(name: String): PagingSource<Int, OrganizationsEntity>

    @Query("DELETE FROM organizationsentity WHERE owner_login=:name")
    suspend fun deleteAll(name: String)

}
