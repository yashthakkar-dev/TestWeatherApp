package com.example.testweatherapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherWithDailyForecast
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: WeatherForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>)

    @Transaction
    suspend fun saveWeatherData(
        weatherWithDailyForecast: WeatherWithDailyForecast
    ) {
        insertForecast(weatherWithDailyForecast.weather)
        insertDailyForecasts(weatherWithDailyForecast.daily)
    }

    @Transaction
    @Query("SELECT * FROM weatherforecastentity LIMIT 1")
    fun getWeatherWithDailyForecast(): Flow<WeatherWithDailyForecast?>

    @Query("SELECT * FROM weatherforecastentity WHERE cityId = :cityId")
    suspend fun getForecast(cityId: Int): WeatherForecastEntity?

    @Query("SELECT * FROM dailyforecastentity WHERE cityId = :cityId")
    fun getDailyForecasts(cityId: Int): Flow<List<DailyForecastEntity>>
}