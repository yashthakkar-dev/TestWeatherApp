package com.example.testweatherapp.data.repository.datasource.entity

import androidx.room.Entity
import com.example.testweatherapp.domain.model.DailyWeatherForecast

@Entity(
    primaryKeys = ["cityId", "date"]
)
data class DailyForecastEntity(
    val cityId: Int,          // foreign key to WeatherForecastEntity
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
) {
    fun asDomainModel(): DailyWeatherForecast =
        DailyWeatherForecast(
            date = date,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = tempDay,
            tempMin = tempMin,
            tempMax = tempMax,
            tempNight = tempNight,
            tempEve = tempEve,
            tempMorn = tempMorn,
            feelsLikeDay = feelsLikeDay,
            feelsLikeNight = feelsLikeNight,
            feelsLikeEve = feelsLikeEve,
            feelsLikeMorn = feelsLikeMorn,
            humidity = humidity,
            pressure = pressure,
            weatherId = weatherId,
            description = description,
            icon = icon,
            speed = speed,
            clouds = clouds,
            rain = rain,
            main = main
        )
}
