package com.example.testweatherapp.domain.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatusProvider {
    fun isNetworkAvailable(): StateFlow<Boolean>
    fun registerNetworkStatus()
    fun unRegisterNetworkStatus()
}