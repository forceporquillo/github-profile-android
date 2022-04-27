package dev.forcecodes.hov.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcecodes.hov.data.cache.entity.KeyIndex

@Dao
interface KeyIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<KeyIndex>)

    @Query("SELECT * FROM keyindex WHERE id=:repoId OR tag=:tag")
    suspend fun remoteKeysRepoId(repoId: Int, tag: String): KeyIndex?

    @Query("DELETE FROM keyindex WHERE tag=:tag")
    suspend fun clearRemoteKeys(tag: String)

}