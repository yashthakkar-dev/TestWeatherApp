package com.example.testweatherapp.data.network.model


import com.example.testweatherapp.data.repository.datasource.entity.DailyForecastEntity
import com.example.testweatherapp.domain.model.DailyWeatherForecast
import com.google.gson.annotations.SerializedName

data class NetworkWeatherItem(
    val clouds: Int,
    val deg: Int,
    @SerializedName("dt")
    val date: Long,
    @SerializedName("feels_like")
    val feelsLike: NetworkFeelsLike,
    val gust: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val speed: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: NetworkTemp,
    val weather: List<NetworkWeatherObject>
) {
    fun asDailyForecastEntity(id: Int): DailyForecastEntity =
        DailyForecastEntity(
            cityId = id,
            date = date,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = temp.day,
            tempMin = temp.min,
            tempMax = temp.max,
            tempNight = temp.night,
            tempEve = temp.eve,
            tempMorn = temp.morn,
            feelsLikeDay = feelsLike.day,
            feelsLikeNight = feelsLike.night,
            feelsLikeEve = feelsLike.eve,
            feelsLikeMorn = feelsLike.morn,
            humidity = humidity,
            pressure = pressure,
            weatherId = weather[0].id,
            description = weather[0].description,
            icon = weather[0].icon,
            speed = speed,
            clouds = clouds,
            rain = rain,
            main = weather[0].main
        )

    fun asDomainModel(): DailyWeatherForecast =
        DailyWeatherForecast(
            date = date,
            sunrise = sunrise,
            sunset = sunset,
            tempDay = temp.day,
            tempMin = temp.min,
            tempMax = temp.max,
            tempNight = temp.night,
            tempEve = temp.eve,
            tempMorn = temp.morn,
            feelsLikeDay = feelsLike.day,
            feelsLikeNight = feelsLike.night,
            feelsLikeEve = feelsLike.eve,
            feelsLikeMorn = feelsLike.morn,
            humidity = humidity,
            pressure = pressure,
            weatherId = weather[0].id,
            description = weather[0].description,
            icon = weather[0].icon,
            speed = speed,
            clouds = clouds,
            rain = rain,
            main = weather[0].main
        )
}
