package com.example.testweatherapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow

object NetworkStatus {
    val isInternetConnected = MutableStateFlow(false)

    private lateinit var connectivityManager: ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (isInternetConnected.value) return
            isInternetConnected.value = true
        }

        override fun onLost(network: Network) {
            if (!isInternetConnected.value) return
            isInternetConnected.value = false
        }
    }

    fun registerNetworkStatus(context: Context) {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregisterNetworkStatus() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
