@file:Suppress("MissingPermission")

package dev.forcecodes.gitprofile.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface NetworkStatusProvider {
    fun provideNetworkConnectionStatus(): NetworkConnectionStatus
}

class DefaultNetworkStatusProvider @Inject constructor(
    @ApplicationContext val context: Context
) : NetworkStatusProvider {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun provideNetworkConnectionStatus(): NetworkConnectionStatus = getNetworkStatus()

    private fun getNetworkStatus(): NetworkConnectionStatus {
        val activeNetwork = connectivityManager.activeNetwork ?: return NetworkConnectionStatus.NotConnectedToInternet
        val networkInfo = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return NetworkConnectionStatus.NotConnectedToInternet
        with(networkInfo) {
            return when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnectionStatus.ConnectedToWifi
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnectionStatus.ConnectedToMobileData
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkConnectionStatus.Other
                else -> NetworkConnectionStatus.NotConnectedToInternet
            }
        }
    }
}
