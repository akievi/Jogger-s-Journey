package com.example.joggersjourney.data.models

data class Quest(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: QuestCategory = QuestCategory.EASY,
    val targetDistance: Double = 0.0, // in km
    val targetTime: Long = 0, // in minutes
    val requiredSpeed: Double = 0.0, // min required average speed in km/h
    val rewardGold: Int = 0,
    val rewardXP: Int = 0,
    val npcName: String = "",
    val npcDialogueIntro: List<String> = emptyList(),
    val npcDialogueWin: List<String> = emptyList(),
    val npcDialogueLoose: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isCompleted: Boolean = false,
    val bestTime: Long = 0, // best completion time in milliseconds
    val bestSpeed: Double = 0.0 // best average speed achieved
)

enum class QuestCategory(val colorCode: String, val difficulty: String, val baseReward: Int) {
    EASY("#4CAF50", "Einfach", 25),
    MEDIUM("#FF9800", "Mittel", 75),
    HARD("#F44336", "Schwer", 150),
    EXPERT("#9C27B0", "Experte", 300)
}

data class ChallengeResult(
    val questId: String,
    val completed: Boolean,
    val timeTaken: Long, // in milliseconds
    val averageSpeed: Double, // in km/h
    val distance: Double, // actual distance covered
    val goldEarned: Int = 0,
    val xpEarned: Int = 0,
    val npcMessage: String = ""
)

data class ChallengeSession(
    val questId: String,
    val startTime: Long = 0,
    val isActive: Boolean = false,
    val elapsedTime: Long = 0,
    val currentSpeed: Double = 0.0,
    val distanceCovered: Double = 0.0,
    val route: List<LocationPoint> = emptyList()
) 