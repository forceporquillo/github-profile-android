@file:Suppress("unused")

package dev.forcecodes.hov.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.hov.data.cache.LocalDataSourceImpl
import dev.forcecodes.hov.data.cache.LocalUserDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindsLocalUserDataSource(
        localDataSource: LocalDataSourceImpl
    ): LocalUserDataSource

}