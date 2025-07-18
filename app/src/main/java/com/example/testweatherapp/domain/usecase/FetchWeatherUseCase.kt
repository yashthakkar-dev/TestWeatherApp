package com.example.testweatherapp.domain.usecase

import com.example.testweatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository)  {
    suspend fun execute(latitude: Double, longitude: Double) = weatherRepository.fetchWeather(latitude, longitude)
}