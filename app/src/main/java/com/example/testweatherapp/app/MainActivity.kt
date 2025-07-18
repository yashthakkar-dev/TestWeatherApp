package com.example.testweatherapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testweatherapp.app.screens.WeatherApp
import com.example.testweatherapp.app.screens.navigation.WeatherNavigation
import com.example.testweatherapp.data.network.NetworkStatus
import com.example.testweatherapp.ui.theme.TestWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestWeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        WeatherNavigation()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        NetworkStatus.registerNetworkStatus(this)
    }

    override fun onStop() {
        super.onStop()
        NetworkStatus.unregisterNetworkStatus()
    }
}
