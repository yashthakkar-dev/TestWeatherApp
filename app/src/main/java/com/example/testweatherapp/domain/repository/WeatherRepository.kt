package com.example.testweatherapp.domain.repository

import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import com.example.testweatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeatherWithDailyForecast(): Flow<Weather?>

    suspend fun fetchWeather(latitude: Double, longitude: Double): Flow<Weather?>
}