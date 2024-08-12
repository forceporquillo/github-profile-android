package dev.forcecodes.gitprofile.data.api.interceptors

import dev.forcecodes.gitprofile.data.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor

internal class HeaderInterceptor(
    private val version: String
) : Interceptor {

    private val credentialsOrNull: String? get() =
        if (BuildConfig.TOKEN.isNotEmpty() || BuildConfig.USERNAME.isNotEmpty()) {
            Credentials.basic(BuildConfig.USERNAME, BuildConfig.TOKEN)
        } else null
    
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val builder = request.newBuilder()
            .addHeader("Accept", version)
            .addHeader("User-Agent", USER_AGENT)
        credentialsOrNull?.let { token ->
            builder.addHeader("Authorization", token)
        }
        return chain.proceed(builder.build())
    }
}

// hardcoded for the meantime
private const val APP_NAME = "dev.forcecodes.android.gitprofile"
private val USER_AGENT by lazy { "developer: ${BuildConfig.USERNAME} app-name: $APP_NAME" }
