package dev.forcecodes.hov.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.hov.domain.source.DetailsRepository
import dev.forcecodes.hov.domain.source.DetailsRepositoryImpl
import dev.forcecodes.hov.domain.source.UserRepositoryImpl
import dev.forcecodes.hov.domain.source.UsersRepository

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