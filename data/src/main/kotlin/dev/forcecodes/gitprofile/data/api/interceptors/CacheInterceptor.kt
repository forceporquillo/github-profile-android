package dev.forcecodes.gitprofile.data.api.interceptors

import okhttp3.CacheControl
import okhttp3.Interceptor

internal class CacheInterceptor(
    private val cacheControl: CacheControl
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return response.newBuilder()
            .removeHeader("Cache-Control")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}
