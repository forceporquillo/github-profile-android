package dev.forcecodes.hov.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.hov.data.internal.InternalApi
import dev.forcecodes.hov.data.paginator.NextPageLinkStore
import dev.forcecodes.hov.data.prefs.LinkPreference
import dev.forcecodes.hov.data.prefs.NextLinkSharedPreference
import dev.forcecodes.hov.data.utils.NextPageIndexer
import dev.forcecodes.hov.data.utils.ResponseNextPageLookup

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