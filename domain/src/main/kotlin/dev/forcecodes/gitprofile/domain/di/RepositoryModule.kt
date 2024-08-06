package dev.forcecodes.gitprofile.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.domain.source.DetailsRepository
import dev.forcecodes.gitprofile.domain.source.DetailsRepositoryImpl
import dev.forcecodes.gitprofile.domain.source.UserRepositoryImpl
import dev.forcecodes.gitprofile.domain.source.UsersRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesGithubRepository(
        githubUserRepository: UserRepositoryImpl
    ): UsersRepository

    @Binds
    abstract fun provideDetailsRepository(
        detailsRepository: DetailsRepositoryImpl
    ): DetailsRepository
}