package com.example.testweatherapp.app.viewmodel

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweatherapp.data.network.NetworkStatus
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.repository.LocationRepository
import com.example.testweatherapp.domain.usecase.FetchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val fetchWeatherUseCase: FetchWeatherUseCase
) : ViewModel() {

    private val location = mutableStateOf<Location?>(null)
    var locationWeatherData = mutableStateOf<Weather?>(null)

    init {
        viewModelScope.launch {
            if (NetworkStatus.isInternetConnected.value && location.value != null) {
                refreshWeatherData()
            }
        }
    }

    fun getLocation() = location.value

    fun refreshWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentLocation = locationRepository.getCurrentLocation()
            location.value = currentLocation

            if (currentLocation != null) {

                val response = fetchWeatherUseCase.execute(
                    currentLocation.latitude,
                    currentLocation.longitude
                )

                withContext(Dispatchers.Main) {
                    locationWeatherData.value = response
                }
            }
        }
    }
}