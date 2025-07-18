package com.example.testweatherapp.data.network.model


data class NetworkTemp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)