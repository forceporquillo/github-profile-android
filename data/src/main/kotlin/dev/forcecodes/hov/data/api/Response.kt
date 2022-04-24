package dev.forcecodes.hov.data.api

import com.squareup.moshi.JsonDataException
import dev.forcecodes.hov.core.internal.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * A Network bound resource implementation.
 *
 * Wraps separately into a function not a [Response.getResponse()] extension,
 * so we can explicitly catch our custom exception needed to throw instead of relying to
 * OkHttp's exception internally.
 */
internal suspend fun <T> getResponse(
    // redundant dispatcher since Retrofit
    // uses handles this internally.
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> Response<T>
): ApiResponse<T> = withContext(dispatcher) {
    try {
        val response = block.invoke()
        val body = response.body()

        if (response.isSuccessful && response.code() == GithubStatusCodes.Ok().status) {
            if (body == null) {
                ApiResponse.Empty("Empty response")
            } else {
                ApiResponse.Success(body)
            }
        } else {
            val forbidden = GithubStatusCodes.Forbidden()

            if (response.code() == forbidden.status) {
                ApiResponse.Error(forbidden.message)
            } else {
                response.parseErrorResponse()
            }
        }
    } catch (e: Exception) {
        e.handleException()
    }
}

/**
 * Generic parsing of error body from a Json response.
 */
private fun <T> Response<T>.parseErrorResponse(): ApiResponse.Error<T> {
    return try {
        // retrieve error body from json response.
        val errorBody = errorBody()!!.charStream().readText()
        return ApiResponse.Error(errorBody)
    } catch (e: JsonDataException) {
        ApiResponse.Error("Failed to parse response from server.")
    } catch (e: Exception) {
        ApiResponse.Error(e.message)
    }
}

/**
 * Handle common network exceptions.
 */
private fun <T> Exception.handleException(): ApiResponse.Error<T> {
    Logger.e(this)
    return when (this) {
        is SocketTimeoutException,
        is IOException,
        is ConnectException,
        is HttpException,
        is UnknownHostException,
        is SocketException -> ApiResponse.Error(
            "Failed to retrieve data from the network.\nPlease check your internet connection and try again.",
            this
        )
        else -> ApiResponse.Error(message ?: "Unknown error. Please try again later.", this)
    }
}

class EmptyResponseException(override val message: String? = "Empty response."): Exception(message)
class ApiErrorResponse(override val message: String?) : Exception(message)