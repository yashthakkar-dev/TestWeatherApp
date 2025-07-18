package com.example.testweatherapp.data.network.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NetworkCity(
    val coord: NetworkCoord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)

