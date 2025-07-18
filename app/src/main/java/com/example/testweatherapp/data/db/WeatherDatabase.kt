package com.example.testweatherapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testweatherapp.domain.util.Constants
import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity

@Database(
    entities = [
        WeatherForecastEntity::class,
        DailyForecastEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)

abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}