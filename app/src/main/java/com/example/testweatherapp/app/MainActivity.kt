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
import com.example.testweatherapp.app.screens.navigation.WeatherNavigation
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import com.example.testweatherapp.ui.theme.TestWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkStatusProvider: NetworkStatusProvider


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
        networkStatusProvider.registerNetworkStatus()
    }

    override fun onStop() {
        super.onStop()
        networkStatusProvider.unRegisterNetworkStatus()
    }
}
