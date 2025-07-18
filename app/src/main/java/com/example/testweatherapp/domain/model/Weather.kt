package com.example.testweatherapp.domain.model

data class Weather(
    val id: Int,
    val cityName: String,
    val country: String,
    val message: Double,
    val cnt: Int,
    val dailyWeatherForecastList: List<DailyWeatherForecast>
)
