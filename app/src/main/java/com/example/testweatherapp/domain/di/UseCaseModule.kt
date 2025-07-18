package com.example.testweatherapp.domain.di

import com.example.testweatherapp.domain.repository.LocationRepository
import com.example.testweatherapp.domain.repository.WeatherRepository
import com.example.testweatherapp.domain.usecase.LocationUseCase
import com.example.testweatherapp.domain.usecase.FetchWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    fun provideWeatherUseCase(weatherRepository: WeatherRepository): FetchWeatherUseCase {
        return FetchWeatherUseCase(weatherRepository)
    }

    @Singleton
    @Provides
    fun provideLocationUseCase(locationRepository: LocationRepository): LocationUseCase {
        return LocationUseCase(locationRepository)
    }
}