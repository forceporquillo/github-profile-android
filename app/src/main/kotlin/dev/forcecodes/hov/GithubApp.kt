package dev.forcecodes.hov

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.forcecodes.hov.data.utils.NextPageIndexer
import javax.inject.Inject

@HiltAndroidApp
class GithubApp : Application() {

    @Inject
    lateinit var nextPageIndexer: NextPageIndexer

    override fun onCreate() {
        super.onCreate()

        // clear next index everytime app stars.
        nextPageIndexer.clear()
    }
}