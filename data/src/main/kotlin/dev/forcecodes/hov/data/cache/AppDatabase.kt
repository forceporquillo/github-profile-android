package dev.forcecodes.hov.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import dev.forcecodes.hov.data.api.models.RepositoryEntity
import dev.forcecodes.hov.data.cache.entity.KeyIndex
import dev.forcecodes.hov.data.cache.entity.OrganizationsEntity
import dev.forcecodes.hov.data.cache.entity.UserDetailsEntity
import dev.forcecodes.hov.data.cache.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        KeyIndex::class,
        UserDetailsEntity::class,
        RepositoryEntity::class,
        OrganizationsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userDetails(): DetailsDao
    abstract fun userRepositories(): UserRepoDao
    abstract fun organizationsDao(): OrganizationsDao
    abstract fun keyIndexDao(): KeyIndexDao
}

suspend inline fun AppDatabase.test(crossinline block: () -> Unit) {
    withTransaction {
        block.invoke()
    }
}