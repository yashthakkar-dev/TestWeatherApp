package com.example.testweatherapp.data.repository.datasourceImpl

import com.example.testweatherapp.data.db.WeatherDao
import com.example.testweatherapp.data.repository.datasource.WeatherLocalDataSource
import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao
) : WeatherLocalDataSource {

    override suspend fun insertForecast(forecast: WeatherForecastEntity) {
        weatherDao.insertForecast(forecast)
    }

    override suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>) {
        weatherDao.insertDailyForecasts(forecasts)
    }

    override suspend fun saveWeatherData(weatherWithDailyForecast: WeatherWithDailyForecast) {
        weatherDao.saveWeatherData(weatherWithDailyForecast)
    }

    override fun getWeatherWithDailyForecast(): Flow<WeatherWithDailyForecast?> {
        return weatherDao.getWeatherWithDailyForecast()
    }

    override suspend fun getForecast(cityId: Int): WeatherForecastEntity? {
        return weatherDao.getForecast(cityId)
    }

    override fun getDailyForecasts(cityId: Int): Flow<List<DailyForecastEntity>> {
        return weatherDao.getDailyForecasts(cityId)
    }


}