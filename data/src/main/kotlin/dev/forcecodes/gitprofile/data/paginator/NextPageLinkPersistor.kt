package dev.forcecodes.gitprofile.data.paginator

import androidx.annotation.MainThread
import dev.forcecodes.gitprofile.core.internal.Logger
import dev.forcecodes.gitprofile.data.prefs.LinkPreference
import dev.forcecodes.gitprofile.data.utils.NextPageIndexer
import dev.forcecodes.gitprofile.data.utils.ResponseNextPageLookup
import okhttp3.Headers
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NextPageLinkStore @Inject constructor(
    private val linkPreference: LinkPreference
) : ResponseNextPageLookup, NextPageIndexer {

    // look up for next page id
    // e.g. https://api.github.com/users?since=5&per_page=30
    private val pattern = Pattern.compile("(?<=since=)\\d+")

    /**
     * Find the next page id from the response header.
     *
     * e.g link: https://api.github.com/users?since=46&per_page=30
     */
    override fun nextIndexFromHeader(headers: Headers?) {
        val linkHeader = headers?.get("link") ?: return
        Logger.i("Saving header-link: $linkHeader")
        val matcher = pattern
            .matcher(linkHeader)

        if (matcher.find()) {
            val nextPage = matcher.group()
            Logger.d("Next load page: $nextPage")
            setNextPage(nextPage.toInt())
        }
    }

    /**
     * Take the maximum index page number and then store and return.
     */
    override fun takeMaxAndStore(index: Int): Int {
        val nextIndex = nextPage()
        val max = maxOf(index, nextIndex)
        setNextPage(max)
        return max
    }

    override fun nextPage(): Int {
        return linkPreference.nextPage ?: INITIAL_PAGE
    }

    private fun setNextPage(next: Int) {
        Logger.i("setNextPage: $next")
        linkPreference.nextPage = next
    }

    @MainThread
    override fun clear() {
        Logger.i("Clearing cache index. Default: $INITIAL_PAGE")
        linkPreference.nextPage = INITIAL_PAGE
    }

    override fun isEqual(index: Int): Boolean {
        val page = nextPage()
        return if (page == index) {
            takeMaxAndStore(page + 1)
            true
        } else {
            false
        }
    }

    companion object {
        private const val INITIAL_PAGE = 0
    }
}