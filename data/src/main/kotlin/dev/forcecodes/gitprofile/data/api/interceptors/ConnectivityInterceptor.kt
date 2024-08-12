package dev.forcecodes.gitprofile.data.api.interceptors

import dev.forcecodes.gitprofile.data.api.NetworkConnectionStatus
import dev.forcecodes.gitprofile.data.api.NetworkStatusProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException

internal class ConnectivityInterceptor(
    private val networkStatusProvider: NetworkStatusProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        when (networkStatusProvider.provideNetworkConnectionStatus()) {
            NetworkConnectionStatus.NotConnectedToInternet -> throw NoInternet
            NetworkConnectionStatus.ConnectionTimeOut -> throw ConnectionTimeOut
            else -> return chain.proceed(chain.request())
        }
    }
}

object NoInternet : IOException() {
    private fun readResolve(): Any = NoInternet
}

object ConnectionTimeOut: SocketTimeoutException() {
    private fun readResolve(): Any = ConnectionTimeOut
}