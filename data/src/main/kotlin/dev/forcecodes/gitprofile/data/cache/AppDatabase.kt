package dev.forcecodes.gitprofile.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.forcecodes.gitprofile.data.api.models.RepositoryEntity
import dev.forcecodes.gitprofile.data.api.models.StarredReposEntity
import dev.forcecodes.gitprofile.data.cache.entity.KeyIndex
import dev.forcecodes.gitprofile.data.cache.entity.OrganizationsEntity
import dev.forcecodes.gitprofile.data.cache.entity.UserDetailsEntity
import dev.forcecodes.gitprofile.data.cache.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        KeyIndex::class,
        UserDetailsEntity::class,
        RepositoryEntity::class,
        StarredReposEntity::class,
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
    abstract fun starredReposDao(): StarredReposDao
    abstract fun keyIndexDao(): KeyIndexDao
}