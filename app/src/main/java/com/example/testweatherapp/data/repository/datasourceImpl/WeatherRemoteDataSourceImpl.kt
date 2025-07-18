package com.example.testweatherapp.data.repository.datasourceImpl

import com.example.testweatherapp.data.network.WeatherApiService
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import com.example.testweatherapp.data.network.model.NetworkWeather
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
): WeatherRemoteDataSource {
    override suspend fun getWeatherByLocation(
        lat: Double,
        lon: Double
    ): NetworkWeather{
        return weatherApiService.getWeatherByLocation(lat, lon)
    }
}