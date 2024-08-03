package dev.forcecodes.hov.domain

sealed interface DataStrategy {

    object Cache : DataStrategy

    object RemoteOverCache: DataStrategy
}