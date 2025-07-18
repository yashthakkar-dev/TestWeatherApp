package com.example.testweatherapp.app.viewmodel

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweatherapp.app.util.Resource
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import com.example.testweatherapp.domain.usecase.WeatherUseCase
import com.example.testweatherapp.domain.usecase.LocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val weatherUseCase: WeatherUseCase,
    networkStatusProvider: NetworkStatusProvider,
) : ViewModel() {

    companion object {
        val TAG: String = WeatherViewModel::class.java.simpleName
    }

    private val location = mutableStateOf<Location?>(null)

    var weatherState = mutableStateOf<Resource<Weather?>>(Resource.Loading())

    val isNetworkAvailable = networkStatusProvider.isNetworkAvailable()

    var job: Job? = null

    fun fetchWeatherData() {
        // Cancel the previous job if it exists
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentLocation = locationUseCase.getCurrentLocation()
                location.value = currentLocation

                currentLocation?.let {
                    withContext(Dispatchers.Main) {
                        showLoading()
                    }

                    //pass current location
                    val weatherData = weatherUseCase.fetchWeather(currentLocation)
                    processData(weatherData)

                } ?: run {
                    //If location is null, fetch data from database
                    val weatherData = weatherUseCase.getWeather()
                    processData(weatherData)
                }
            } catch (e: Exception) {
                weatherState.value = Resource.Error(e.message ?: "Error fetching location data")
            }
        }
    }

    suspend fun processData(weatherData: Flow<Weather?>) {
        Log.d(TAG, "processData: $weatherData")
        weatherData.catch {
            withContext(Dispatchers.Main) {
                weatherState.value = Resource.Error(parseError(it))
            }
        }.collect { weather ->
            withContext(Dispatchers.Main) {
                weatherState.value = Resource.Success(weather)
            }
        }
    }

    fun parseError(throwable: Throwable): String {
        Log.d(TAG, "parseError: $throwable")
        return when (throwable) {
            is HttpException -> {
                "HTTP Error: ${throwable.code()}"
            }

            else -> {
                "Error: ${throwable.message}"
            }
        }
    }

    fun showLoading() {
        weatherState.value = Resource.Loading()
    }
}