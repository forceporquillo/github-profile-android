package dev.forcecodes.gitprofile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.data.api.DefaultNetworkStatusProvider
import dev.forcecodes.gitprofile.data.api.NetworkStatusProvider
import dev.forcecodes.gitprofile.data.internal.InternalApi

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkStatusModule {

    @InternalApi
    @Binds
    abstract fun bindNetworkStateProvider(
        defaultNetworkStatusProvider: DefaultNetworkStatusProvider
    ): NetworkStatusProvider
}
