package com.example.joggersjourney.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.joggersjourney.ui.screens.AvatarScreen
import com.example.joggersjourney.ui.screens.MapScreen
import com.example.joggersjourney.ui.screens.ProfilScreen
import com.example.joggersjourney.ui.screens.QuestDetailScreen
import com.example.joggersjourney.ui.screens.RunningSessionScreen
import com.example.joggersjourney.ui.screens.SettingsScreen
import com.example.joggersjourney.ui.screens.ShopScreen
import com.example.joggersjourney.ui.screens.SocialScreen
import com.example.joggersjourney.ui.screens.StatisticsScreen

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

// Neue 3-Tab Navigation: Statistik | Journey | Profil
val bottomNavItems = listOf(
    BottomNavItem(Screen.Statistics, Icons.Default.Build, "Statistik"),
    BottomNavItem(Screen.Map, Icons.Default.LocationOn, "Journey"),
    BottomNavItem(Screen.Profil, Icons.Default.AccountCircle, "Profil")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoggersJourneyNavigation(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            
            // Nur Bottom Navigation zeigen für Hauptscreens
            val showBottomBar = currentRoute in listOf("map", "statistics", "profil")
            
            if (showBottomBar) {
                // Gaming-Style Bottom Navigation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F0F23)) // Sehr dunkler Hintergrund
                        .padding(4.dp) // Für Border-Effekt
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF1A1A2E), // Gaming-Hintergrund
                                RoundedCornerShape(8.dp)
                            ),
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                icon = { 
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                if (currentRoute == item.screen.route) 
                                                    Color(0xFF00AAFF) // Neon-Dunkelblau für aktiv statt Grün
                                                else Color(0xFF2A2A4A), // Dunkler für inaktiv
                                                RoundedCornerShape(4.dp) // Verpixelt
                                            )
                                            .padding(2.dp)
                                            .background(
                                                if (currentRoute == item.screen.route)
                                                    Color(0xFF001133) // Dunkles Blau statt Grün
                                                else Color(0xFF1A1A2E),
                                                RoundedCornerShape(2.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            item.icon, 
                                            contentDescription = item.label,
                                            tint = if (currentRoute == item.screen.route)
                                                Color(0xFF00AAFF) // Neon-Dunkelblau für aktiv statt Grün
                                            else Color(0xFF888888), // Grau für inaktiv
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                },
                                label = { 
                                    Text(
                                        item.label,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentRoute == item.screen.route)
                                            Color(0xFF00FFFF) // Cyan für aktiv
                                        else Color(0xFF888888) // Grau für inaktiv
                                    )
                                },
                                selected = currentRoute == item.screen.route,
                                onClick = {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Transparent,
                                    unselectedIconColor = Color.Transparent,
                                    selectedTextColor = Color.Transparent,
                                    unselectedTextColor = Color.Transparent,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Haupttabs
            composable(Screen.Map.route) {
                MapScreen(navController = navController)
            }
            composable(Screen.Statistics.route) {
                StatisticsScreen(navController = navController)
            }
            composable(Screen.Profil.route) {
                ProfilScreen(navController = navController)
            }
            
            // Unterseiten (über Profil erreichbar)
            composable(Screen.Avatar.route) {
                AvatarScreen(navController = navController)
            }
            composable(Screen.Shop.route) {
                ShopScreen(navController = navController)
            }
            composable(Screen.Social.route) {
                SocialScreen(navController = navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
            
            // Quest-spezifische Screens
            composable(Screen.QuestDetail.route) { backStackEntry ->
                val questId = backStackEntry.arguments?.getString("questId") ?: ""
                QuestDetailScreen(questId = questId, navController = navController)
            }
            composable(Screen.RunningSession.route) { backStackEntry ->
                val questId = backStackEntry.arguments?.getString("questId") ?: ""
                RunningSessionScreen(questId = questId, navController = navController)
            }
        }
    }
} 