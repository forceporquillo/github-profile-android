package dev.forcecodes.hov.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.hov.domain.source.UserDetailsKeyIndexPaginator
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object UserDetailsKeyIndexPaginatorModule {

    @Provides
    @Named(DETAILS_REPOS)
    fun providesRepositoriesKeyPaginator(): UserDetailsKeyIndexPaginator {
        return UserDetailsKeyIndexPaginator()
    }

    @Provides
    @Named(STARRED_REPOS)
    fun providesStarredReposKeyPaginator(): UserDetailsKeyIndexPaginator {
        return UserDetailsKeyIndexPaginator()
    }

    @Provides
    @Named(USER_ORGS)
    fun providesUserOrgsKeyPaginator(): UserDetailsKeyIndexPaginator {
        return UserDetailsKeyIndexPaginator()
    }
}

internal const val DETAILS_REPOS = "detais_repos"
internal const val STARRED_REPOS = "starred_repos"
internal const val USER_ORGS = "user_orgs"