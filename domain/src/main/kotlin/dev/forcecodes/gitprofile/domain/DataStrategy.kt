package dev.forcecodes.gitprofile.domain

sealed interface DataStrategy {
    data object RemoteOverCache : DataStrategy
    data object CacheOverRemote : DataStrategy
}