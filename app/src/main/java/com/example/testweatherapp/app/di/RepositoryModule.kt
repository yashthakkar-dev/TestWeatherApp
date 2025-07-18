package com.example.testweatherapp.app.di

import android.content.Context
import com.example.testweatherapp.data.location.LocationProvider
import com.example.testweatherapp.data.repository.LocationRepositoryImpl
import com.example.testweatherapp.data.repository.WeatherRepositoryImpl
import com.example.testweatherapp.data.repository.datasource.WeatherLocalDataSource
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import com.example.testweatherapp.domain.repository.LocationRepository
import com.example.testweatherapp.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherRemoteDataSource: WeatherRemoteDataSource,
        weatherLocalDataSource: WeatherLocalDataSource
    ): WeatherRepository{
        return WeatherRepositoryImpl(weatherRemoteDataSource, weatherLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideLocationProvider(
        @ApplicationContext context: Context
    ): LocationProvider {
        return LocationProvider(context)
    }

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationProvider: LocationProvider
    ): LocationRepository {
        return LocationRepositoryImpl(locationProvider)
    }
}