@file:Suppress("unused")

package dev.forcecodes.hov.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

/**
 * A generic class that holds a value with its loading status.
 * @param R the type object.
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val exception: Exception, val data: T? = null) : Result<T>()
    data class Loading<out T>(val data: T? = null) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading<*> -> "Loading"
        }
    }
}

/**
 * Converts the [Result] to [Flow].
 */
fun <T> Result<T>.flow(loading: Boolean = false): Flow<Result<T>> {
    return flow {
        if (loading) {
            emit(Result.Loading())
        }
        emit(this@flow)
    }
}

/**
 * Converts [Any] to cold a stream of flow [Result.Success].
 */
fun <T : Any> T.asSuccessFlow() = flow<Result<T>> {
    emit(Result.Success(this@asSuccessFlow))
}

/**
 * Converts the [Result] to [Flow] which emits [Result.Loading] as initial return.
 */

fun <T : Result<K>, K> loadingFlow(
    show: Boolean = true,
    block: suspend () -> T
): Flow<Result<K>> {
    return flow {
        if (show) {
            emit(Result.Loading())
        }
        emit(block.invoke())
    }
}

/**
 * Collects the [Result] optionally.
 */
inline fun <T> Result<T>.collectAsState(
    loading: (T?) -> Unit = {},
    success: (T) -> Unit = {},
    error: (Exception) -> Unit = {}
) {
    return when (this) {
        is Result.Loading -> loading.invoke(data)
        is Result.Success -> success.invoke(data)
        is Result.Error -> error.invoke(exception)
    }
}

/**
 * `true` if [Result] is type of [Result.Success] & holds non-null [Result.Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

/**
 * returns [Result.data] if type of [Result.Success] or fallback to [T] value.
 */
fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

/**
 * `true` if [Result] is type of [Result.Loading].
 */
inline fun <T> Result<T>.isLoadingState(block: (Boolean) -> Unit) {
    block(this is Result.Loading<*>)
}

/**
 * returns nullable [Result] of [Result.data]. Otherwise, null.
 */
val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data

/**
 * `true` if [Result] is of type [Result.Error].
 */
val <T> Result<T>.error: java.lang.Exception
    get() = (this as Result.Error).exception

/**
 * asserts the [Result] to a non-nullable [Result.Success] & holds non-null [Result.Success.data]. Otherwise, throws [Result.error].
 */
val <T> Result<T>.notNullData: T
    get() = (this as Result.Success).data ?: throw error

/**
 * Updates value of [MutableStateFlow] if [Result] is of type [Result.Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(stateFlow: MutableStateFlow<T>) {
    if (this is Result.Success) {
        stateFlow.value = data
    }
}