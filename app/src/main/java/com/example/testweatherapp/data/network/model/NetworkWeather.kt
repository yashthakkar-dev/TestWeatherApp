package com.example.testweatherapp.data.network.model


import androidx.room.PrimaryKey
import com.example.testweatherapp.data.repository.datasource.entity.WeatherForecastEntity
import com.example.testweatherapp.domain.model.DailyWeatherForecast
import com.example.testweatherapp.domain.model.Weather


data class NetworkWeather(
    @PrimaryKey val id: Int,
    val city: NetworkCity,
    val cnt: Int,
    val cod: String,
    val list: List<NetworkWeatherItem>,
    val message: Double
) {
    fun asForecastEntity(): WeatherForecastEntity =
        WeatherForecastEntity(
            cityId = city.id,
            cityName = city.name,
            country = city.country,
            message = message,
            cnt = cnt
        )

    fun asDomainModel(dailyWeatherForecastList: List<DailyWeatherForecast>): Weather {
        return Weather(
            id = id,
            cityName = city.name,
            country = city.country,
            message = message,
            cnt = cnt,
            dailyWeatherForecastList = dailyWeatherForecastList
        )
    }
}

















