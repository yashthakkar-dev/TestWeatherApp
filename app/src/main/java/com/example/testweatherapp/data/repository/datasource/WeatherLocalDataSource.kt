package com.example.testweatherapp.data.repository.datasource

import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun saveWeatherData(weatherWithDailyForecast: WeatherWithDailyForecast)
    suspend fun deleteWeatherData()

    fun getWeatherWithDailyForecast(): Flow<WeatherWithDailyForecast?>

}