package dev.forcecodes.hov.data

import dev.forcecodes.hov.data.paginator.NextPageLinkStore
import dev.forcecodes.hov.data.prefs.LinkPreference
import okhttp3.Headers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NextPageLinkStoreTest {

    private lateinit var nextPageLinkStore: NextPageLinkStore

    @Before
    fun before() {
        nextPageLinkStore = NextPageLinkStore(FakePrefs())
    }

    @Test
    fun `test initial page index is zero`() {
        Assert.assertEquals(0, nextPageLinkStore.nextPage())
    }

    @Test
    fun `test fake parse of index from header`() {
       val header = Headers.headersOf("link", "https://api.github.com/users?since=46&per_page=30")
        nextPageLinkStore.nextIndexFromHeader(header)

        Assert.assertEquals(46, nextPageLinkStore.nextPage())
    }

    @Test
    fun `test should fail parse of index headers`() {
        val header = Headers.headersOf("link", "https://api.github.com/users?since=426&per_page=30")
        nextPageLinkStore.nextIndexFromHeader(header)

        Assert.assertNotEquals(123, nextPageLinkStore.nextPage())
    }

    @Test
    fun `test should get the max value and store`() {
        // fake store of header
        val header = Headers.headersOf("link", "https://api.github.com/users?since=1&per_page=30")
        nextPageLinkStore.nextIndexFromHeader(header)

        Assert.assertEquals(1, nextPageLinkStore.nextPage())

        val nextPage = nextPageLinkStore.takeMaxAndStore(120)

        Assert.assertEquals(120, nextPageLinkStore.nextPage())
        Assert.assertEquals(120, nextPage)
    }

    @Test
    fun `clearing cache should reset to zero`() {
        nextPageLinkStore.clear()
        Assert.assertEquals(0, nextPageLinkStore.nextPage())
    }

    @Test
    fun `check equal and load max page index`() {
        nextPageLinkStore.takeMaxAndStore(120)
        nextPageLinkStore.isEqual(30)

        Assert.assertEquals(false,  nextPageLinkStore.isEqual(30))
        Assert.assertEquals(120, nextPageLinkStore.nextPage())

        nextPageLinkStore.takeMaxAndStore(120)
        Assert.assertEquals(120, nextPageLinkStore.nextPage())
    }

    private class FakePrefs : LinkPreference {

        private var pageIndex: Int? = 0

        override var nextPage: Int? = pageIndex
            set(value) {
                if (field != value) {
                    if (value != null) {
                        pageIndex = field
                        field = value
                    }
                }
            }
    }
}