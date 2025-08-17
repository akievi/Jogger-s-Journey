package com.example.joggersjourney.data

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import com.example.joggersjourney.data.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.*

class ChallengeManager(private val audioManager: AudioManager) {
    
    // Current challenge session
    private val _currentSession = MutableStateFlow<ChallengeSession?>(null)
    val currentSession: StateFlow<ChallengeSession?> = _currentSession
    
    // Real-time tracking data
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime
    
    private val _currentSpeed = MutableStateFlow(0.0)
    val currentSpeed: StateFlow<Double> = _currentSpeed
    
    private val _distanceCovered = MutableStateFlow(0.0)
    val distanceCovered: StateFlow<Double> = _distanceCovered
    
    private var timerJob: Job? = null
    private var speedCheckJob: Job? = null
    private var lastSpeedCheckTime = 0L
    private var previousLocation: Location? = null
    private val locationHistory = mutableListOf<LocationPoint>()
    
    fun startChallenge(quest: Quest): Boolean {
        if (_currentSession.value?.isActive == true) {
            return false // Already has active challenge
        }
        
        val session = ChallengeSession(
            questId = quest.id,
            startTime = System.currentTimeMillis(),
            isActive = true
        )
        
        _currentSession.value = session
        _elapsedTime.value = 0L
        _distanceCovered.value = 0.0
        _currentSpeed.value = 0.0
        locationHistory.clear()
        previousLocation = null
        
        // Start timer
        startTimer()
        
        // Start speed monitoring
        startSpeedMonitoring(quest)
        
        // Play start sound
        audioManager.playChallengeStart()
        
        return true
    }
    
    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            val startTime = System.currentTimeMillis()
            while (_currentSession.value?.isActive == true) {
                delay(100) // Update every 100ms for smooth timer
                _elapsedTime.value = System.currentTimeMillis() - startTime
            }
        }
    }
    
    private fun startSpeedMonitoring(quest: Quest) {
        speedCheckJob = CoroutineScope(Dispatchers.Main).launch {
            while (_currentSession.value?.isActive == true) {
                delay(5000) // Check speed every 5 seconds
                
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastSpeedCheckTime >= 10000) { // Only check after 10 seconds
                    checkSpeedFeedback(quest)
                    lastSpeedCheckTime = currentTime
                }
            }
        }
    }
    
    private fun checkSpeedFeedback(quest: Quest) {
        val avgSpeed = calculateAverageSpeed()
        val requiredSpeed = quest.requiredSpeed
        
        if (requiredSpeed > 0 && avgSpeed > 0) {
            if (avgSpeed < requiredSpeed * 0.8) { // 20% below required speed
                audioManager.playSpeedWarning()
            } else if (avgSpeed >= requiredSpeed) {
                audioManager.playSpeedGood()
            }
        }
    }
    
    fun updateLocation(latitude: Double, longitude: Double, speed: Double = 0.0) {
        val session = _currentSession.value ?: return
        if (!session.isActive) return
        
        val currentTime = System.currentTimeMillis()
        val newLocation = Location("gps").apply {
            this.latitude = latitude
            this.longitude = longitude
            this.speed = speed.toFloat()
            time = currentTime
        }
        
        // Add to location history
        val locationPoint = LocationPoint(
            latitude = latitude,
            longitude = longitude,
            timestamp = currentTime,
            speed = speed
        )
        locationHistory.add(locationPoint)
        
        // Calculate distance if we have a previous location
        previousLocation?.let { prevLoc ->
            val distance = prevLoc.distanceTo(newLocation) / 1000.0 // Convert to km
            _distanceCovered.value += distance
        }
        
        // Update current speed
        _currentSpeed.value = speed
        
        previousLocation = newLocation
        
        // Update session
        _currentSession.value = session.copy(
            elapsedTime = currentTime - session.startTime,
            currentSpeed = speed,
            distanceCovered = _distanceCovered.value,
            route = locationHistory.toList()
        )
    }
    
    fun completeChallenge(quest: Quest): ChallengeResult {
        val session = _currentSession.value ?: return createFailedResult(quest.id)
        
        // Stop tracking
        stopTracking()
        
        val timeTaken = _elapsedTime.value
        val distance = _distanceCovered.value
        val avgSpeed = calculateAverageSpeed()
        
        // Determine if challenge was successful
        val timeSuccess = quest.targetTime <= 0 || timeTaken <= quest.targetTime * 60 * 1000
        val distanceSuccess = quest.targetDistance <= 0 || distance >= quest.targetDistance
        val speedSuccess = quest.requiredSpeed <= 0 || avgSpeed >= quest.requiredSpeed
        
        val isCompleted = timeSuccess && distanceSuccess && speedSuccess
        
        // Calculate rewards
        val baseReward = quest.category.baseReward
        val goldEarned = if (isCompleted) quest.rewardGold else 0
        val xpEarned = if (isCompleted) quest.rewardXP else 0
        
        // Get random NPC message
        val messages = if (isCompleted) quest.npcDialogueWin else quest.npcDialogueLoose
        val npcMessage = if (messages.isNotEmpty()) messages.random() else ""
        
        // Play result sound
        if (isCompleted) {
            audioManager.playChallengeSuccess()
        } else {
            audioManager.playChallengeFail()
        }
        
        val result = ChallengeResult(
            questId = quest.id,
            completed = isCompleted,
            timeTaken = timeTaken,
            averageSpeed = avgSpeed,
            distance = distance,
            goldEarned = goldEarned,
            xpEarned = xpEarned,
            npcMessage = npcMessage
        )
        
        _currentSession.value = null
        
        return result
    }
    
    fun cancelChallenge() {
        stopTracking()
        _currentSession.value = null
    }
    
    private fun stopTracking() {
        timerJob?.cancel()
        speedCheckJob?.cancel()
        timerJob = null
        speedCheckJob = null
    }
    
    private fun calculateAverageSpeed(): Double {
        val distance = _distanceCovered.value
        val timeInHours = _elapsedTime.value / (1000.0 * 60.0 * 60.0)
        
        return if (timeInHours > 0 && distance > 0) {
            distance / timeInHours
        } else {
            0.0
        }
    }
    
    private fun createFailedResult(questId: String): ChallengeResult {
        return ChallengeResult(
            questId = questId,
            completed = false,
            timeTaken = 0,
            averageSpeed = 0.0,
            distance = 0.0,
            npcMessage = "Challenge konnte nicht gestartet werden!"
        )
    }
    
    fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60))
        
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
    
    fun formatSpeed(speed: Double): String {
        return String.format("%.1f km/h", speed)
    }
    
    fun formatDistance(distance: Double): String {
        return if (distance < 1.0) {
            String.format("%.0f m", distance * 1000)
        } else {
            String.format("%.2f km", distance)
        }
    }
    
    private fun calculateSpeed(distance: Double, timeInSeconds: Double): Double {
        return if (timeInSeconds > 0) distance / timeInSeconds else 0.0 // Returns speed in m/s
    }

    private fun isSpeedWithinRange(currentSpeed: Double, requiredSpeed: Double, tolerance: Double = 0.5): Boolean {
        return currentSpeed >= (requiredSpeed - tolerance) && currentSpeed <= (requiredSpeed + tolerance)
    }

    fun getCurrentSpeed(): Double {
        return if (_elapsedTime.value > 0) {
            calculateSpeed(_distanceCovered.value, _elapsedTime.value / 1000.0)
        } else {
            0.0
        }
    }

    fun getSpeedInKmh(): Double {
        return getCurrentSpeed() * 3.6 // Convert m/s to km/h
    }
} 