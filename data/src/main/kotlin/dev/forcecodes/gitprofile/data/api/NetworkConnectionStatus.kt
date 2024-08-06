package dev.forcecodes.gitprofile.data.api

sealed interface NetworkConnectionStatus {
    data object ConnectedToMobileData: NetworkConnectionStatus
    data object ConnectedToWifi: NetworkConnectionStatus
    data object NotConnectedToInternet: NetworkConnectionStatus
    data object ConnectionTimeOut: NetworkConnectionStatus
    data object Other: NetworkConnectionStatus
}
