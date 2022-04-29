package dev.forcecodes.hov.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.hov.data.prefs.DataStorePreferenceStorage
import dev.forcecodes.hov.data.prefs.PreferenceStorage

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceStorageModule {

    @Binds
    abstract fun providesPreferenceStorage(
        datastore: DataStorePreferenceStorage
    ): PreferenceStorage
}