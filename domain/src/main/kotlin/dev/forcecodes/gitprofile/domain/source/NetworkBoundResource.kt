package dev.forcecodes.gitprofile.domain.source

import dev.forcecodes.gitprofile.core.Result
import dev.forcecodes.gitprofile.core.internal.Logger
import dev.forcecodes.gitprofile.data.api.ApiErrorResponse
import dev.forcecodes.gitprofile.data.api.ApiResponse
import dev.forcecodes.gitprofile.data.api.EmptyResponseException
import dev.forcecodes.gitprofile.data.api.onEmpty
import dev.forcecodes.gitprofile.data.api.onError
import dev.forcecodes.gitprofile.data.api.onSuccess
import kotlinx.coroutines.flow.*

typealias RemoteNetworkCall<Remote> = suspend () -> ApiResponse<Remote>
typealias LocalDatabaseCache<Cache> = suspend () -> Flow<Cache>
typealias ResponseAccumulator<Remote> = suspend (Remote) -> Unit

sealed interface FailureStrategy {
    data object ThrowSilently : FailureStrategy
    data object ThrowOnFailure: FailureStrategy
}

sealed interface FetchBehavior {
    data object FetchSilently : FetchBehavior
    data object FetchWithProgress : FetchBehavior
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

    internal fun <Local, Remote> conflateResource(
        fetchBehavior: FetchBehavior = FetchBehavior.FetchWithProgress,
        strategy: FailureStrategy = FailureStrategy.ThrowSilently,
        cacheSource: LocalDatabaseCache<Local>,
        remoteSource: RemoteNetworkCall<Remote>,
        accumulator: ResponseAccumulator<Remote>,
        shouldFetch: (Local?) -> Boolean = { true }
    ): Flow<Result<Local>> = flow {

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