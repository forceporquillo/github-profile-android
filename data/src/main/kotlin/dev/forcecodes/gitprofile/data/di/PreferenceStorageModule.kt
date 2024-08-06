package dev.forcecodes.gitprofile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.data.prefs.DataStorePreferenceStorage
import dev.forcecodes.gitprofile.data.prefs.PreferenceStorage

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceStorageModule {

    @Binds
    abstract fun providesPreferenceStorage(
        datastore: DataStorePreferenceStorage
    ): PreferenceStorage
}