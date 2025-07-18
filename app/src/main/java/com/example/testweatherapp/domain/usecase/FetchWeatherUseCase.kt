package com.example.testweatherapp.domain.usecase

import android.location.Location
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(location: Location): Flow<Weather?> {
        return weatherRepository.fetchWeather(location.latitude, location.longitude)
    }

}