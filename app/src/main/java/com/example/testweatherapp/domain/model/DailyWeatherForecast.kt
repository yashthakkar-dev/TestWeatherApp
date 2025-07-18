package com.example.testweatherapp.domain.model

data class DailyWeatherForecast(
    val date: Long,
    val sunrise: Int,
    val sunset: Int,
    val tempDay: Double,
    val tempMin: Double,
    val tempMax: Double,
    val tempNight: Double,
    val tempEve: Double,
    val tempMorn: Double,
    val feelsLikeDay: Double,
    val feelsLikeNight: Double,
    val feelsLikeEve: Double,
    val feelsLikeMorn: Double,
    val humidity: Int,
    val pressure: Int,
    val weatherId: Int,
    val description: String,
    val icon: String,
    val speed: Double,
    val clouds: Int,
    val rain: Double?,
    val main: String
)
