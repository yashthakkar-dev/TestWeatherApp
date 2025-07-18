package com.example.testweatherapp.data.repository.datasource

import com.example.testweatherapp.data.network.model.NetworkWeather
import retrofit2.Call
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getWeatherByLocation(lat: Double, lon: Double): NetworkWeather
}