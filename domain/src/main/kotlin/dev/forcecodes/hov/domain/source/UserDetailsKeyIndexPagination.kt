package dev.forcecodes.hov.domain.source

import android.os.Build
import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Workaround paging implementation.
 */
@Singleton
class UserDetailsKeyIndexPagination @Inject constructor() : NetworkBoundResource() {

    private val userPageMap = HashMap<String, Int>()

    @PublishedApi
    internal fun <Remote, Cache> requestData(
        userName: String,
        cacheSource: () -> Flow<Cache>,
        remoteSource: suspend (page: Int) -> ApiResponse<Remote>,
        saveFetchResult: suspend (Remote) -> Unit,
    ): Flow<Result<Cache>> = flow {
        val lastPage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            userPageMap.getOrDefault(userName, 1)
        } else {
            userPageMap[userName] ?: 1
        }
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