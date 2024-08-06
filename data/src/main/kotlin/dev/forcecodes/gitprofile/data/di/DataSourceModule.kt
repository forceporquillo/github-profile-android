@file:Suppress("unused")

package dev.forcecodes.gitprofile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.data.cache.LocalDataSourceImpl
import dev.forcecodes.gitprofile.data.cache.LocalUserDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindsLocalUserDataSource(
        localDataSource: LocalDataSourceImpl
    ): LocalUserDataSource

}