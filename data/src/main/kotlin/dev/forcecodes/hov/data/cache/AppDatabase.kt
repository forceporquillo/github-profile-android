package dev.forcecodes.hov.data.cache

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.data.cache.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SinceKeys::class,
        UserDetailsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userDetails(): DetailsDao
    abstract fun sincePagerIndexDao(): SincePagerIndexDao
}

@Dao
interface SincePagerIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<SinceKeys>)

    @Query("SELECT * FROM sincekeys WHERE id = :repoId")
    suspend fun remoteKeysRepoId(repoId: Int): SinceKeys?

    @Query("DELETE FROM sincekeys")
    suspend fun clearRemoteKeys()

}

@Entity
data class SinceKeys(
    @PrimaryKey
    val id: Int,

    val prevSince: Int?,
    val nextSince: Int?
)
