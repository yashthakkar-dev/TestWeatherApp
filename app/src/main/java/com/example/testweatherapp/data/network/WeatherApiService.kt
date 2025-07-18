package com.example.testweatherapp.data.network

import com.example.testweatherapp.BuildConfig
import com.example.testweatherapp.data.network.model.NetworkWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int = 7,
        @Query("appid") appid: String = BuildConfig.API_KEY,
//        @Query("units") units: String = "metric"
    ): NetworkWeather
}