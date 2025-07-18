package com.example.testweatherapp.domain.usecase

import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    operator fun invoke(): Flow<Weather?> {
        return weatherRepository.getWeatherWithDailyForecast()
    }
}