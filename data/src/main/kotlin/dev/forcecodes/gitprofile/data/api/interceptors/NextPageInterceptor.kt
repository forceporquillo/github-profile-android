package dev.forcecodes.gitprofile.data.api.interceptors

import dev.forcecodes.gitprofile.data.extensions.containsNextPage
import dev.forcecodes.gitprofile.data.utils.ResponseNextPageLookup
import okhttp3.Interceptor

internal class NextPageInterceptor(
    private val responseNextPageLookup: ResponseNextPageLookup
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response = chain.proceed(chain.request())
        if (response.containsNextPage) {
            responseNextPageLookup.nextIndexFromHeader(response.headers)
        }
        return response
    }
}
