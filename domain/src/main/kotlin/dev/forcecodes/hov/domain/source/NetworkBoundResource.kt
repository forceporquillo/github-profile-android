package dev.forcecodes.hov.domain.source

import dev.forcecodes.hov.core.Result
import dev.forcecodes.hov.core.internal.Logger
import dev.forcecodes.hov.data.api.ApiErrorResponse
import dev.forcecodes.hov.data.api.ApiResponse
import dev.forcecodes.hov.data.api.EmptyResponseException
import dev.forcecodes.hov.data.api.onEmpty
import dev.forcecodes.hov.data.api.onError
import dev.forcecodes.hov.data.api.onSuccess
import dev.forcecodes.hov.domain.FailureStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

typealias RemoteNetworkCall<Remote> = suspend () -> ApiResponse<Remote>
typealias LocalDatabaseCache<Cache> = suspend () -> Flow<Cache>
typealias ResponseAccumulator<Remote> = suspend (Remote) -> Unit

sealed interface FetchBehavior {
    object FetchSilently : FetchBehavior
    object FetchWithProgress : FetchBehavior
}

val FetchBehavior.isFetchWithProgress: Boolean
    get() = this is FetchBehavior.FetchWithProgress

suspend fun <Remote, Response> RemoteNetworkCall<Remote>.execute(
    collector: suspend (Remote) -> Response
) {
    invoke()
        .onSuccess {
            collector.invoke(this)
        }
        .onError { errorMessage, exception ->
            Logger.e(exception, errorMessage)
            throw ApiErrorResponse(errorMessage)
        }.onEmpty { emptyMessage ->
            Logger.e(emptyMessage)
            throw EmptyResponseException(emptyMessage)
        }
}

open class NetworkBoundResource {

    internal fun <Cache, Remote> conflateResource(
        fetchBehavior: FetchBehavior = FetchBehavior.FetchWithProgress,
        strategy: FailureStrategy = FailureStrategy.ThrowSilently,
        cacheSource: LocalDatabaseCache<Cache>,
        remoteSource: RemoteNetworkCall<Remote>,
        accumulator: ResponseAccumulator<Remote>,
        shouldFetch: (Cache?) -> Boolean = { true }
    ): Flow<Result<Cache>> = flow {

        if (fetchBehavior.isFetchWithProgress) {
            emit(Result.Loading())
        }

        val cache = cacheSource()

        val executeRemoteRequest: suspend () -> Unit = {
            remoteSource.execute(accumulator)
        }

        if (shouldFetch(cache.firstOrNull())) {
            kotlin.runCatching {
                executeRemoteRequest()
            }.onFailure { throwable ->
                if (strategy is FailureStrategy.ThrowOnFailure) {
                    throw throwable
                }
            }
        }

        emitAll(cache.map { Result.Success(it) })
    }
}