package com.example.testweatherapp.app.viewmodel

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweatherapp.data.Resource
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import com.example.testweatherapp.domain.usecase.LocationUseCase
import com.example.testweatherapp.domain.usecase.FetchWeatherUseCase
import com.example.testweatherapp.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import retrofit2.HttpException

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    networkStatusProvider: NetworkStatusProvider,
) : ViewModel() {

    private val location = mutableStateOf<Location?>(null)

    var weatherState = mutableStateOf<Resource<Weather?>>(Resource.Loading())

    val isNetworkAvailable = networkStatusProvider.isNetworkAvailable()

    var job: Job? = null

    fun fetchWeatherData() {
        // Cancel the previous job if it exists
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {

            val currentLocation = locationUseCase.invoke()
            location.value = currentLocation

            currentLocation?.let {
                withContext(Dispatchers.Main) {
                    showLoading()
                }

                //pass current location
                val weatherData = fetchWeatherUseCase.invoke(currentLocation)
                processData(weatherData)

            } ?: run {
                val weatherData = getWeatherUseCase.invoke()
                processData(weatherData)
            }
        }
    }

    suspend fun processData(weatherData: Flow<Weather?>) {
        Log.d("WeatherViewModel", "processData: $weatherData")
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
        Log.d("WeatherViewModel", "parseError: $throwable")
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