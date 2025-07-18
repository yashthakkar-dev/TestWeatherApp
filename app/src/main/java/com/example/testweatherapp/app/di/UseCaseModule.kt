package com.example.testweatherapp.app.di

import com.example.testweatherapp.domain.repository.WeatherRepository
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
    fun provideFetchWeatherUseCase(weatherRepository: WeatherRepository): FetchWeatherUseCase {
        return FetchWeatherUseCase(weatherRepository)
    }
}