package dev.forcecodes.gitprofile.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.data.cache.AppDatabase
import dev.forcecodes.gitprofile.data.internal.InternalApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @InternalApi
    @Provides
    @Singleton
    fun providesAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "AppDb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesUserDao(@InternalApi appDatabase: AppDatabase) = appDatabase.userDao()

    @Singleton
    @Provides
    fun providesUserDetailsDao(@InternalApi appDatabase: AppDatabase) = appDatabase.userDetails()

    @Singleton
    @Provides
    fun providesKeyIndexDao(@InternalApi appDatabase: AppDatabase) = appDatabase.keyIndexDao()
}