package com.example.testweatherapp.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
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

        val lastLocation = fusedLocationClient.lastLocation.await() // Use kotlinx-coroutines-play-services
        if (lastLocation != null) {
            Log.d(TAG, "Returning last known location: $lastLocation")
            return@withTimeoutOrNull lastLocation
        }

        // If not available, proceed to request active location
        suspendCancellableCoroutine { cont ->
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000
            ).apply {
                setWaitForAccurateLocation(false)
                setMaxUpdates(5)
            }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    if (cont.isActive && result.locations.isNotEmpty()) {
                        Log.d(TAG, "Location found: ${result.lastLocation}")
                        fusedLocationClient.removeLocationUpdates(this)
//                        cont.resume(result.lastLocation)
                    }
                }
                override fun onLocationAvailability(availability: LocationAvailability) {
                    if (cont.isActive && !availability.isLocationAvailable) {
                        Log.d(TAG, "Location not available")
                        fusedLocationClient.removeLocationUpdates(this)
//                        cont.resume(null)
                    }
                }
            }

            cont.invokeOnCancellation {
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