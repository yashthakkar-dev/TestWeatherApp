package com.example.testweatherapp.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationProvider @Inject constructor(
    context: Context
) {
    companion object {
        val TAG: String = LocationProvider::class.java.simpleName
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? = withTimeoutOrNull(10000L) {

        val lastLocation = fusedLocationClient.lastLocation.await()
        Log.d(TAG, "Last known location (fallback): $lastLocation")

        // proceed to request active location
        suspendCancellableCoroutine { continuation ->
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000
            ).setWaitForAccurateLocation(false)
                .setMaxUpdates(5)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    if (continuation.isActive && result.locations.isNotEmpty()) {
                        Log.d(TAG, "Location found: ${result.lastLocation}")
                        fusedLocationClient.removeLocationUpdates(this)
                        result.lastLocation?.let {
                            continuation.resume(it)
                        } ?: run {
                            Log.d(TAG, "Falling back to last known location")
                            if (lastLocation != null) {
                                continuation.resume(lastLocation)
                            } else {
                                continuation.resumeWith(Result.failure(NullPointerException("Location is null")))
                            }
                        }
                    }
                }

                override fun onLocationAvailability(availability: LocationAvailability) {
                    if (continuation.isActive && !availability.isLocationAvailable) {
                        Log.d(TAG, "Location not available")
                        fusedLocationClient.removeLocationUpdates(this)
                        Log.d(TAG, "Falling back to last known location")
                        if (lastLocation != null) {
                            continuation.resume(lastLocation)
                        } else {
                            continuation.resumeWith(Result.failure(NullPointerException("Location unavailable")))
                        }
                    }
                }
            }

            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }

            Log.d(TAG, "Requesting location updates")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }
}