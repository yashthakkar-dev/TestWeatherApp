package com.example.testweatherapp.data.repository

import android.util.Log
import com.example.testweatherapp.data.network.NetworkStatus
import com.example.testweatherapp.data.repository.datasource.WeatherLocalDataSource
import com.example.testweatherapp.data.repository.datasource.WeatherRemoteDataSource
import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import com.example.testweatherapp.domain.model.Weather
import com.example.testweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) : WeatherRepository {

    override suspend fun insertForecast(forecast: WeatherForecastEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun saveWeatherData(weatherWithDailyForecast: WeatherWithDailyForecast) {
        TODO("Not yet implemented")
    }

    override fun getWeatherWithDailyForecast(): Flow<WeatherWithDailyForecast?> {
        return weatherLocalDataSource.getWeatherWithDailyForecast()
    }

    override suspend fun getForecast(cityId: Int): WeatherForecastEntity? {
        TODO("Not yet implemented")
    }

    override fun getDailyForecasts(cityId: Int): Flow<List<DailyForecastEntity>> {
        TODO("Not yet implemented")
    }


    override suspend fun fetchWeather(
        latitude: Double,
        longitude: Double
    ): Flow<Weather?> {
        if (NetworkStatus.isInternetConnected.value) {
            val response = weatherRemoteDataSource.getWeatherByLocation(
                lat = latitude,
                lon = longitude
            )
            response.let { weatherResponse ->
                val weatherWithDailyForecast = WeatherWithDailyForecast(
                    weather = weatherResponse.asForecastEntity(),
                    daily = weatherResponse.list.map { weatherItem ->
                        weatherItem.asDailyForecastEntity(weatherResponse.city.id)
                    }
                )
                Log.d("yash", "weatherWithDailyForecast $weatherWithDailyForecast")
                weatherLocalDataSource.saveWeatherData(
                    weatherWithDailyForecast
                )
            }
            return flow {
                val list = response.list.map { it.asDomainModel() }
                emit(response.asDomainModel(list))
            }
        } else {
            return weatherLocalDataSource.getWeatherWithDailyForecast()
                .map { it?.asDomainModel() }
        }
    }
}