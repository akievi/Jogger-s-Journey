package com.example.joggersjourney.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationService(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    private val locationSimulator = LocationSimulator()
    private var isSimulationMode = false

    fun enableSimulationMode() {
        isSimulationMode = true
    }

    fun disableSimulationMode() {
        isSimulationMode = false
        locationSimulator.stopSimulation()
    }

    fun startSimulation(targetSpeed: Double) {
        if (isSimulationMode) {
            locationSimulator.startSimulation(targetSpeed)
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun getCurrentLocation(): LatLng? {
        return try {
            if (isSimulationMode) {
                return LatLng(locationSimulator.getCurrentSpeed(), locationSimulator.getCurrentSpeed())
            }
            
            if (!hasLocationPermission()) {
                println("DEBUG: Keine Location-Berechtigung")
                return null
            }
            
            println("DEBUG: Versuche aktuelle Location zu bekommen...")
            
            // Erst versuchen wir die letzte bekannte Location
            val lastLocation = fusedLocationClient.lastLocation.await()
            println("DEBUG: Letzte bekannte Location: $lastLocation")
            
            // Wenn keine letzte Location oder älter als 5 Minuten, neue Location anfordern
            val shouldRequestFresh = lastLocation == null || 
                (System.currentTimeMillis() - lastLocation.time) > 5 * 60 * 1000
            
            if (shouldRequestFresh) {
                println("DEBUG: Fordere frische Location an...")
                return requestFreshLocation()
            }
            
            lastLocation?.let { 
                val result = LatLng(it.latitude, it.longitude)
                println("DEBUG: Verwende cached Location: $result")
                result
            }
        } catch (e: Exception) {
            println("DEBUG: Fehler beim Location abrufen: ${e.message}")
            null
        }
    }
    
    private suspend fun requestFreshLocation(): LatLng? {
        return try {
            println("DEBUG: Starte frische Location-Anfrage...")
            
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                0L // Sofort
            ).apply {
                setMaxUpdateDelayMillis(10000L) // Max 10 Sekunden warten
                setMinUpdateIntervalMillis(0L) // Sofort
            }.build()
            
            // Warten auf frische Location
            kotlinx.coroutines.withTimeoutOrNull(15000L) { // 15 Sekunden Timeout
                kotlinx.coroutines.suspendCancellableCoroutine<LatLng?> { cont ->
                    var hasResponded = false
                    
                    val callback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            if (!hasResponded) {
                                hasResponded = true
                                val location = result.lastLocation
                                fusedLocationClient.removeLocationUpdates(this)
                                
                                if (location != null) {
                                    val latLng = LatLng(location.latitude, location.longitude)
                                    println("DEBUG: Frische Location erhalten: $latLng (Genauigkeit: ${location.accuracy}m)")
                                    cont.resumeWith(Result.success(latLng))
                                } else {
                                    println("DEBUG: Keine frische Location erhalten")
                                    cont.resumeWith(Result.success(null))
                                }
                            }
                        }
                        
                        override fun onLocationAvailability(availability: LocationAvailability) {
                            if (!availability.isLocationAvailable && !hasResponded) {
                                hasResponded = true
                                fusedLocationClient.removeLocationUpdates(this)
                                println("DEBUG: Location nicht verfügbar")
                                cont.resumeWith(Result.success(null))
                            }
                        }
                    }
                    
                    fusedLocationClient.requestLocationUpdates(locationRequest, callback, null)
                    
                    cont.invokeOnCancellation {
                        fusedLocationClient.removeLocationUpdates(callback)
                    }
                }
            } ?: run {
                println("DEBUG: Timeout bei frischer Location-Anfrage")
                null
            }
        } catch (e: Exception) {
            println("DEBUG: Fehler bei frischer Location-Anfrage: ${e.message}")
            null
        }
    }

    fun getLocationUpdates(): Flow<LatLng> = callbackFlow {
        if (isSimulationMode) {
            locationSimulator.getSimulatedLocationUpdates().collect { location ->
                trySend(location)
            }
            return@callbackFlow
        }

        if (!hasLocationPermission()) {
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L // 10 seconds
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(LatLng(location.latitude, location.longitude))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    companion object {
        // Standardkoordinaten für Gummersbach, Stinmüllerallee (falls keine GPS-Berechtigung)
        val DEFAULT_LOCATION = LatLng(51.0269, 7.5636) // Gummersbach, Nordrhein-Westfalen
    }
} 