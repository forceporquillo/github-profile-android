package dev.forcecodes.hov.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.hov.data.cache.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity")
    fun getUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUsers(users: List<UserEntity>)

    @Query("DELETE FROM userentity")
    suspend fun clearUsers()

    @Query("SELECT * FROM userentity")
    fun getPagingUsers(): PagingSource<Int, UserEntity>
}
