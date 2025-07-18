package com.example.testweatherapp.data.repository.datasource

import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insertForecast(forecast: WeatherForecastEntity)

    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>)

    suspend fun saveWeatherData(weatherWithDailyForecast: WeatherWithDailyForecast)

    fun getWeatherWithDailyForecast(): Flow<WeatherWithDailyForecast?>

    suspend fun getForecast(cityId: Int): WeatherForecastEntity?

    fun getDailyForecasts(cityId: Int): Flow<List<DailyForecastEntity>>

}