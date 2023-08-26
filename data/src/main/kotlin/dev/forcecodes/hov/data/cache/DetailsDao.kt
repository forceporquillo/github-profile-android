package dev.forcecodes.hov.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDetails(details: UserDetailsEntity)

    @Query("SELECT * FROM userdetailsentity WHERE id=:id")
    fun getDetails(id: Int): Flow<UserDetailsEntity>

    @Query("SELECT * FROM userdetailsentity WHERE (displayName LIKE :name OR name LIKE :name) AND displayName IS NOT NULL AND name IS NOT NULL")
    fun getDetails(name: String): Flow<List<UserDetailsEntity>>

}
