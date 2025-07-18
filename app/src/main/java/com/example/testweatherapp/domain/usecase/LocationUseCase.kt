package com.example.testweatherapp.domain.usecase

import android.location.Location
import com.example.testweatherapp.domain.repository.LocationRepository
import javax.inject.Inject

class LocationUseCase @Inject constructor(private val locationRepository: LocationRepository)  {
    suspend fun getCurrentLocation(): Location? {
        return locationRepository.getCurrentLocation()
    }
}