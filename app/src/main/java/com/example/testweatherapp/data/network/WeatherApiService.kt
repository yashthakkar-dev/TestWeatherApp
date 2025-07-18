package com.example.testweatherapp.data.network

import com.example.testweatherapp.BuildConfig
import com.example.testweatherapp.data.network.model.NetworkWeather
import com.example.testweatherapp.domain.util.Constants.DAYS_COUNT
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET(value = "data/2.5/forecast/daily")
    suspend fun fetchWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int = DAYS_COUNT,
        @Query("appid") appid: String = BuildConfig.API_KEY,
        //This is deprecated api, sometimes application crashes if units added
        @Query("units") units: String = "metric"
    ): NetworkWeather
}