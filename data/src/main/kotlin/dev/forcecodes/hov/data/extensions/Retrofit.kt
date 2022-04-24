package dev.forcecodes.hov.data.extensions

import dagger.Lazy
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

@PublishedApi
internal inline fun Retrofit.Builder.callFactory(
    crossinline body: (Request) -> okhttp3.Call
) = callFactory { request -> body(request) }

@Suppress("NOTHING_TO_INLINE")
inline fun Retrofit.Builder.delegatingCallFactory(
    delegate: Lazy<OkHttpClient>
): Retrofit.Builder = callFactory {
    delegate.get().newCall(it)
}