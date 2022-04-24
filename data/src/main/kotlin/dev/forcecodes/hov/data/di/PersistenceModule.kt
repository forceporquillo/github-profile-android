package dev.forcecodes.hov.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.hov.data.cache.AppDatabase
import dev.forcecodes.hov.data.internal.InternalApi
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
}