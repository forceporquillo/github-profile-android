package dev.forcecodes.gitprofile.data.internal

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class GithubApi

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class InternalApi