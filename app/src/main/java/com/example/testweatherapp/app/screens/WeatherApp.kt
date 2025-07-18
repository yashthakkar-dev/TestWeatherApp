package com.example.testweatherapp.app.screens

import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testweatherapp.app.screens.components.MainContent
import com.example.testweatherapp.app.viewmodel.WeatherViewModel
import com.example.testweatherapp.data.Resource
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherApp(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val weatherState = viewModel.weatherState.value

    val isConnected by viewModel.isNetworkAvailable.collectAsState()

    LaunchedEffect(isConnected) {
        if (isConnected && viewModel.getLocation() != null) {
            viewModel.refreshWeatherData()
        }
    }

    when (val state = weatherState) {
        is Resource.Loading -> CircularProgressIndicator()
        is Resource.Error -> {
            Toast.makeText(
                context,
                state.message ?: "An unexpected error occurred",
                Toast.LENGTH_LONG
            ).show()
        }
        is Resource.Success -> state.data?.let {
            MainContent(navController = navController, data = it)
        }
    }
}





