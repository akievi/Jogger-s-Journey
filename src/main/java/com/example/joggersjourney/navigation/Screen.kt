package com.example.joggersjourney.navigation

sealed class Screen(val route: String) {
    object Map : Screen("map")
    object Avatar : Screen("avatar")
    object Profil : Screen("profil")
    
    // Unterseiten die vom Profil aus erreichbar sind
    object Shop : Screen("shop")
    object Social : Screen("social")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
    object QuestDetail : Screen("quest_detail/{questId}") {
        fun createRoute(questId: String) = "quest_detail/$questId"
    }
    object RunningSession : Screen("running_session/{questId}") {
        fun createRoute(questId: String) = "running_session/$questId"
    }
} 