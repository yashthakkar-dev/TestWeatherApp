package com.example.testweatherapp.data.repository.datasource.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.testweatherapp.domain.model.Weather

data class WeatherWithDailyForecast(
    @Embedded val weather: WeatherForecastEntity,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityId",
        entity = DailyForecastEntity::class
    )
    val daily: List<DailyForecastEntity>
) {
    fun asDomainModel(): Weather {
        return Weather(
            id = weather.cityId,
            cityName = weather.cityName,
            country = weather.country,
            message = weather.message,
            cnt = weather.cnt,
            dailyWeatherForecastList = daily.map { it.asDomainModel() }
        )
    }
}
