package com.example.testweatherapp.data.repository

import android.util.Log
import com.example.testweatherapp.data.repository.datasource.WeatherLocalDataSource
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import com.example.testweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val networkStatusProvider: NetworkStatusProvider,
) : WeatherRepository {

    override fun getWeatherWithDailyForecast(): Flow<Weather?> {
        val weatherWithDailyForecast = weatherLocalDataSource.getWeatherWithDailyForecast()
        return weatherWithDailyForecast.map { it?.asDomainModel() }
    }

    override suspend fun fetchWeather(latitude: Double, longitude: Double): Flow<Weather?> {

        if (networkStatusProvider.isNetworkAvailable().value) {
            val response = weatherRemoteDataSource.fetchWeatherByLocation(lat = latitude, lon = longitude)

            return response.transform {
                val weatherWithDailyForecast = WeatherWithDailyForecast(
                    weather = it.asForecastEntity(),
                    daily = it.list.map { weatherItem ->
                        weatherItem.asDailyForecastEntity(it.city.id)
                    }
                )
                Log.d("WeatherRepositoryImpl", "weatherWithDailyForecast $weatherWithDailyForecast")
                this.emit(weatherWithDailyForecast.asDomainModel())
                weatherLocalDataSource.deleteWeatherData()
                weatherLocalDataSource.saveWeatherData(weatherWithDailyForecast)
            }
        } else {
            return weatherLocalDataSource.getWeatherWithDailyForecast()
                .map { it?.asDomainModel() }
        }
    }
}