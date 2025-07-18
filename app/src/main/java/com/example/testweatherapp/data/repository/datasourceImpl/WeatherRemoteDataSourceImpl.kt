package com.example.testweatherapp.data.repository.datasourceImpl

import com.example.testweatherapp.data.network.WeatherApiService
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
): WeatherRemoteDataSource {

    companion object {
        const val DAYS_COUNT = 7
    }

    override suspend fun fetchWeatherByLocation(lat: Double, lon: Double) = flow {
        emit(weatherApiService.fetchWeatherByLocation(lat, lon))
    }
}