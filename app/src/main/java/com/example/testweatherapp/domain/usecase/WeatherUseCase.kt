package com.example.testweatherapp.domain.usecase

import android.location.Location
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun fetchWeather(location: Location): Flow<Weather?> {
        return weatherRepository.fetchWeather(location.latitude, location.longitude)
    }

    fun getWeather(): Flow<Weather?> {
        return weatherRepository.getWeatherWithDailyForecast()
    }

}