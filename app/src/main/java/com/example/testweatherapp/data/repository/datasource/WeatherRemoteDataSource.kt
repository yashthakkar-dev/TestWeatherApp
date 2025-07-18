package com.example.testweatherapp.data.repository.datasource

import com.example.testweatherapp.data.network.model.NetworkWeather
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun fetchWeatherByLocation(lat: Double, lon: Double): Flow<NetworkWeather>
}