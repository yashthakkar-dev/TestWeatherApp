package com.example.testweatherapp.app.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testweatherapp.app.screens.components.MainContent
import com.example.testweatherapp.app.viewmodel.WeatherViewModel
import com.example.testweatherapp.data.network.NetworkStatus
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherApp(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val weatherState = viewModel.locationWeatherData.value

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            viewModel.refreshWeatherData()
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    val isConnected by NetworkStatus.isInternetConnected.collectAsState()

    LaunchedEffect(isConnected) {
        if (isConnected && viewModel.getLocation() != null) {
            viewModel.refreshWeatherData()
        }
    }

    if (weatherState != null) {
        MainContent(
            navController = navController, data = weatherState
        )
    }
}





