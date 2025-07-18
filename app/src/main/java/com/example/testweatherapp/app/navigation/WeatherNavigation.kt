package com.example.testweatherapp.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testweatherapp.app.screens.AboutScreen
import com.example.testweatherapp.app.screens.WeatherAppScreen
import com.example.testweatherapp.app.screens.components.PermissionWrapper

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WeatherScreens.RequestPermission.name
    ) {

        composable(WeatherScreens.RequestPermission.name) { navBack ->
            PermissionWrapper(
                onPermissionGranted = {
                    navController.navigate(WeatherScreens.MainScreen.name) {
                        popUpTo(WeatherScreens.RequestPermission.name) { inclusive = true } // Remove permission from back stack
                    }
                }
            )
        }

        composable(WeatherScreens.MainScreen.name) { navBack ->
            WeatherAppScreen(navController)
        }

        composable(WeatherScreens.AboutScreen.name) {
            AboutScreen(navController = navController)
        }
    }
}