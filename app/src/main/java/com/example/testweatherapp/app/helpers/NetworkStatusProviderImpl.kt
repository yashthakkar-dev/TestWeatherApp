package com.example.testweatherapp.app.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NetworkStatusProviderImpl @Inject constructor(@ApplicationContext private val context: Context) : NetworkStatusProvider {

    // StateFlow to hold the network connectivity status
    private val isNetworkAvailable = MutableStateFlow(false)

    private lateinit var connectivityManager: ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (isNetworkAvailable.value) return
            isNetworkAvailable.value = true
        }

        override fun onLost(network: Network) {
            if (!isNetworkAvailable.value) return
            isNetworkAvailable.value = false
        }
    }

    override fun isNetworkAvailable(): StateFlow<Boolean> {
        return isNetworkAvailable
    }

    override fun registerNetworkStatus() {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun unRegisterNetworkStatus() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
