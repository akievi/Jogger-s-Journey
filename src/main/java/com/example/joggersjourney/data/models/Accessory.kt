package com.example.joggersjourney.data.models

data class Accessory(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val type: AccessoryType = AccessoryType.HAT,
    val price: Int = 0,
    val imageUrl: String = "",
    val isUnlocked: Boolean = false
)

enum class AccessoryType {
    HAT,
    SHIRT,
    PANTS,
    SHOES,
    GLASSES,
    BACKPACK
} 