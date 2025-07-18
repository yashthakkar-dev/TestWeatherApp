package com.example.testweatherapp.data.repository

import android.location.Location
import com.example.testweatherapp.data.location.LocationProvider
import com.example.testweatherapp.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationProvider: LocationProvider
) : LocationRepository {
    override suspend fun getCurrentLocation(): Location? {
        return locationProvider.getCurrentLocation()
    }
}