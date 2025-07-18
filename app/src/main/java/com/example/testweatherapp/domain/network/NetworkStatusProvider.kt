package com.example.testweatherapp.domain.network

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

interface NetworkStatusProvider {
    fun isNetworkAvailable(): StateFlow<Boolean>
    fun registerNetworkStatus(context: Context)
    fun unRegisterNetworkStatus()
}