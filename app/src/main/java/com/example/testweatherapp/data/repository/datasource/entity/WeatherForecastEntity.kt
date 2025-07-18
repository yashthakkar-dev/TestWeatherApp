package com.example.testweatherapp.data.repository.datasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherForecastEntity(
    @PrimaryKey val cityId: Int,
    val cityName: String,
    val country: String,
    val message: Double,
    val cnt: Int
)