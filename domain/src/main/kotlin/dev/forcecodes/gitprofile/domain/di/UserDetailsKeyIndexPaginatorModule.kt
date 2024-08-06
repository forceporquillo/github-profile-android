package dev.forcecodes.gitprofile.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.domain.source.UserDetailsKeyIndexPagination
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object UserDetailsKeyIndexPaginatorModule {

    @Provides
    @Named(DETAILS_REPOS)
    fun providesRepositoriesKeyPaginator(): UserDetailsKeyIndexPagination {
        return UserDetailsKeyIndexPagination()
    }

    @Provides
    @Named(STARRED_REPOS)
    fun providesStarredReposKeyPaginator(): UserDetailsKeyIndexPagination {
        return UserDetailsKeyIndexPagination()
    }

    @Provides
    @Named(USER_ORGS)
    fun providesUserOrgsKeyPaginator(): UserDetailsKeyIndexPagination {
        return UserDetailsKeyIndexPagination()
    }
}

internal const val DETAILS_REPOS = "detais_repos"
internal const val STARRED_REPOS = "starred_repos"
internal const val USER_ORGS = "user_orgs"