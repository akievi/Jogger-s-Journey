package com.example.joggersjourney.data.models

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val avatarAccessories: List<String> = emptyList(),
    val goldBalance: Int = 0,
    val xpPoints: Int = 0,
    val level: Int = 1,
    val friends: List<String> = emptyList(),
    val isPremium: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) 