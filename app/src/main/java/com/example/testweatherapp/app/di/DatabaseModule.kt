package com.example.testweatherapp.app.di

import android.app.Application
import androidx.room.Room
import com.example.testweatherapp.data.network.WeatherApiService
import com.example.testweatherapp.data.db.WeatherDao
import com.example.testweatherapp.data.db.WeatherDatabase
import com.example.testweatherapp.data.repository.datasource.WeatherLocalDataSource
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import com.example.testweatherapp.data.repository.datasourceImpl.WeatherLocalDataSourceImpl
import com.example.testweatherapp.data.repository.datasourceImpl.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideWeatherDatabase(app: Application): WeatherDatabase{
        return Room.databaseBuilder(app, WeatherDatabase::class.java,"weather_db")
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.getWeatherDao()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(weatherDao: WeatherDao): WeatherLocalDataSource{
        return WeatherLocalDataSourceImpl(weatherDao)
    }

    @Singleton
    @Provides
    fun provideWeatherRemoteDatasource(weatherApiService: WeatherApiService): WeatherRemoteDataSource{
        return WeatherRemoteDataSourceImpl(weatherApiService)
    }
}