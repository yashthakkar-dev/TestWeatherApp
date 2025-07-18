package com.example.testweatherapp.app.screens

import android.Manifest
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testweatherapp.R
import com.example.testweatherapp.app.screens.components.MainContent
import com.example.testweatherapp.app.viewmodel.WeatherViewModel
import com.example.testweatherapp.app.util.Resource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherAppScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val weatherState = viewModel.weatherState.value

    val isConnected by viewModel.isNetworkAvailable.collectAsState()

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            viewModel.fetchWeatherData()
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            if (permissionState.allPermissionsGranted) {
                viewModel.fetchWeatherData()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.please_grant_location_permission_to_get_weather_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            //no need to re-fetch weather data on network change if not connected
        }
    }

    when (val state = weatherState) {
        is Resource.Loading -> CircularProgressIndicator()
        is Resource.Error -> {
            Toast.makeText(
                context,
                state.message,
                Toast.LENGTH_LONG
            ).show()
        }

        is Resource.Success -> state.data?.let {
            MainContent(navController = navController, data = it)
        }
    }
}





