package com.example.joggersjourney.data.models

data class RunSession(
    val id: String = "",
    val userId: String = "",
    val questId: String? = null,
    val startTime: Long = 0,
    val endTime: Long = 0,
    val duration: Long = 0, // in milliseconds
    val distance: Double = 0.0, // in km
    val averageSpeed: Double = 0.0, // in km/h
    val maxSpeed: Double = 0.0, // in km/h
    val calories: Int = 0,
    val route: List<LocationPoint> = emptyList(),
    val isQuestCompleted: Boolean = false,
    val goldEarned: Int = 0,
    val xpEarned: Int = 0
)

data class LocationPoint(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Long = 0,
    val speed: Double = 0.0
) 