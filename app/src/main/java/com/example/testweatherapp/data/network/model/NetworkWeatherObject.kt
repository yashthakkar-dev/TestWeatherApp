package com.example.testweatherapp.data.network.model


data class NetworkWeatherObject(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)