package com.example.testweatherapp.domain.usecase

import android.location.Location
import com.example.testweatherapp.domain.repository.LocationRepository
import javax.inject.Inject

class LocationUseCase @Inject constructor(private val locationRepository: LocationRepository)  {
    suspend operator fun invoke(): Location? {
        return locationRepository.getCurrentLocation()
    }
}