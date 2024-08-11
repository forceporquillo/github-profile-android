package dev.forcecodes.gitprofile.domain.source

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.data.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the registration and fetching of paged data for different types of instances.
 *
 * This class provides a workaround implementation for paging, allowing the registration
 * of pages for different types of instances and managing the API calls and caching of the results.
 *
 */
@Singleton
class KeyedPagedIndexManager @Inject constructor() {

    private val fetcherInstanceMap = ConcurrentHashMap<String, PageFetcher>()

    /**
     * Registers a page for a given type of instance and returns a `PageFetcher` instance.
     *
     * @param type The type of instance for which the page is being registered.
     * @return A `PageFetcher` instance for the given type.
     */
    fun registerPage(type: String): PageFetcher {
        return fetcherInstanceMap.computeIfAbsent(type) { PageFetcherImpl() }
    }

    /**
     * Interface representing a page fetcher.
     */
    interface PageFetcher {

        /**
         * Manages the API call and stores the result in the persistence storage.
         *
         * @param userName The name of the user for whom the data is being requested.
         * @param cacheSource A lambda function that provides a Flow of cached data.
         * @param remoteSource A suspend function that fetches data from the remote source.
         * @param saveFetchResult A suspend function that saves the fetched data to the database.
         * @return A Flow emitting the result of the data request, either from the cache or the remote source.
         *
         * @param Remote The type of the data fetched from the remote source.
         * @param Cache The type of the data stored in the cache.
         */
        fun <Remote, Cache> requestData(
            userName: String,
            cacheSource: () -> Flow<Cache>,
            remoteSource: suspend (page: Int) -> ApiResponse<Remote>,
            saveFetchResult: suspend (Remote) -> Unit
        ): Flow<Result<Cache>>
    }

    /**
     * Implementation of the `PageFetcher` interface that built on top of the `NetworkBoundResource` class.
     */
    private class PageFetcherImpl : NetworkBoundResource(), PageFetcher {

        private val userPageMap = HashMap<String, Int>()

        /**
         * Manages the API call and stores the result in the persistence storage.
         *
         * @param userName The name of the user for whom the data is being requested.
         * @param cacheSource A lambda function that provides a Flow of cached data.
         * @param remoteSource A suspend function that fetches data from the remote source.
         * @param saveFetchResult A suspend function that saves the fetched data to the database.
         * @return A Flow emitting the result of the data request, either from the cache or the remote source.
         *
         * @param Remote The type of the data fetched from the remote source.
         * @param Cache The type of the data stored in the cache.
         */
        override fun <Remote, Cache> requestData(
            userName: String,
            cacheSource: () -> Flow<Cache>,
            remoteSource: suspend (page: Int) -> ApiResponse<Remote>,
            saveFetchResult: suspend (Remote) -> Unit
        ): Flow<Result<Cache>> = flow {
            val lastPage = userPageMap.getOrDefault(userName, 1)
            conflateResource(
                fetchBehavior = FetchBehavior.FetchSilently,
                cacheSource = { cacheSource.invoke() },
                remoteSource = { remoteSource.invoke(lastPage) },
                accumulator = {
                    userPageMap[userName] = lastPage + 1
                    saveFetchResult.invoke(it)
                }
            ).collect(this)
        }
    }
}