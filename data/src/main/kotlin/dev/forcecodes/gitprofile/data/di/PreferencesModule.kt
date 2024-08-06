package dev.forcecodes.gitprofile.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.gitprofile.data.internal.InternalApi
import dev.forcecodes.gitprofile.data.paginator.NextPageLinkStore
import dev.forcecodes.gitprofile.data.prefs.LinkPreference
import dev.forcecodes.gitprofile.data.prefs.NextLinkSharedPreference
import dev.forcecodes.gitprofile.data.utils.NextPageIndexer
import dev.forcecodes.gitprofile.data.utils.ResponseNextPageLookup

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @InternalApi
    @Binds
    abstract fun bindsNextPagerLookup(
        nextPageLinkPersistor: NextPageLinkStore
    ): ResponseNextPageLookup

    @Binds
    abstract fun bindsNextPagerIndexer(
        nextPageLinkPersistor: NextPageLinkStore
    ): NextPageIndexer

    @Binds
    abstract fun bindNextLinkPreference(
        nextLinkSharedPreference: NextLinkSharedPreference
    ): LinkPreference
}