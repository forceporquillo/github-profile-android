package dev.forcecodes.hov.domain.source

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Workaround paging implementation.
 */
@Singleton
class UserDetailsKeyIndexPaginator @Inject constructor() : NetworkBoundResource() {

    private var lastPage = 1

    private var pageName: String? = null

    private var isRequestInProgress = false

    @PublishedApi
    internal fun <Remote, Cache> requestData(
        name: String,
        cacheSource: () -> Flow<Cache>,
        remoteSource: suspend (page: Int) -> ApiResponse<Remote>,
        saveFetchResult: suspend (Remote) -> Unit,
    ): Flow<Result<Cache>> = flow {

        if (isRequestInProgress) {
            //return@flow emit(Result.Success(cacheSource.invoke().first()))
        }

        if (pageName == name) {
            Logger.e("Adding new emitter: $name $lastPage")
        } else {
            pageName = name
            lastPage = 1
            Logger.e("OnLoadMore: $name $lastPage")
        }

        emitAll(
            conflateResource(
                false,
                cacheSource = {
                    cacheSource.invoke()
                },
                remoteSource = {
                    isRequestInProgress = true
                    remoteSource.invoke(lastPage)
                },
                saveFetchResult = {
                    lastPage++
                    saveFetchResult.invoke(it)
                }
            )
        )
    }
}