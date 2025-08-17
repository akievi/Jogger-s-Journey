package com.example.joggersjourney.data

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.cos
import kotlin.math.sin

class LocationSimulator {
    private var currentLatitude = 51.0269 // Start in Gummersbach
    private var currentLongitude = 7.5636
    private var currentSpeed = 0.0 // m/s
    private var isSimulating = false
    private var targetSpeed = 0.0 // m/s
    private var direction = 0.0 // in radians

    fun startSimulation(targetSpeed: Double) {
        this.targetSpeed = targetSpeed
        this.isSimulating = true
        this.direction = Math.random() * 2 * Math.PI // Random direction
    }

    fun stopSimulation() {
        isSimulating = false
        currentSpeed = 0.0
    }

    fun getSimulatedLocationUpdates(): Flow<LatLng> = flow {
        while (isSimulating) {
            // Gradually adjust speed towards target
            currentSpeed = currentSpeed + (targetSpeed - currentSpeed) * 0.1

            // Calculate new position based on current speed and direction
            // Speed is in m/s, so we need to convert to degrees per second
            val speedInDegreesPerSecond = (currentSpeed * 3.6) / 111.32 // Convert m/s to km/h, then to degrees
            
            // Update position
            currentLatitude += speedInDegreesPerSecond * cos(direction) / 3600 // per second
            currentLongitude += speedInDegreesPerSecond * sin(direction) / (3600 * cos(Math.toRadians(currentLatitude)))

            // Add some random movement to make it more realistic
            direction += (Math.random() - 0.5) * 0.1
            if (direction > 2 * Math.PI) direction -= 2 * Math.PI
            if (direction < 0) direction += 2 * Math.PI

            emit(LatLng(currentLatitude, currentLongitude))
            delay(1000) // Update every second
        }
    }

    fun getCurrentSpeed(): Double = currentSpeed

    companion object {
        // Predefined routes for testing
        val TEST_ROUTES = mapOf(
            "easy_jogger1" to listOf(
                LatLng(51.0269, 7.5636),
                LatLng(51.0270, 7.5638),
                LatLng(51.0271, 7.5640),
                LatLng(51.0272, 7.5642)
            ),
            "medium_runner" to listOf(
                LatLng(51.0269, 7.5636),
                LatLng(51.0275, 7.5640),
                LatLng(51.0280, 7.5645),
                LatLng(51.0285, 7.5650)
            ),
            "hard_marathon" to listOf(
                LatLng(51.0269, 7.5636),
                LatLng(51.0280, 7.5650),
                LatLng(51.0290, 7.5665),
                LatLng(51.0300, 7.5680)
            )
        )
    }
} 