package com.example.joggersjourney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E)) // Gaming-Hintergrund
    ) {
        // Gaming-Style Top Bar
        TopAppBar(
            title = { 
                Text(
                    "👤 SPIELER PROFIL", 
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                ) 
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF0F0F23)
            )
        )
        
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Player Avatar & Basic Info Card
            PlayerInfoCard()
            
            // Level & XP Progress Card
            LevelProgressCard()
            
            // Stats Overview Card
            StatsOverviewCard()
            
            // Achievements Card
            AchievementsCard()
            
            // Recent Activity Card
            RecentActivityCard()
        }
    }
}

@Composable
fun PlayerInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A4A))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Player Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6600CC)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏃‍♂️",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Player Name
            Text(
                text = "LÄUFER_MEISTER_2024",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFFF),
                textAlign = TextAlign.Center
            )
            
            // Player Title
            Text(
                text = "🎮 AKTIVER JOGGER",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatItem("💰", "Gold", "1,250")
                QuickStatItem("⭐", "Gesamt XP", "3,750")
                QuickStatItem("🏆", "Challenges", "12")
            }
        }
    }
}

@Composable
fun LevelProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2A0F))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LEVEL 7",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FF00)
                )
                Text(
                    text = "2,750 / 4,000 XP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCCCCCC)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // XP Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color(0xFF2A2A2A), RoundedCornerShape(10.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.69f) // 69% progress (2750/4000)
                        .fillMaxHeight()
                        .background(
                            Color(0xFF00FFFF),
                            RoundedCornerShape(10.dp)
                        )
                )
                
                // Progress Text
                Text(
                    text = "1,250 XP bis Level 8",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StatsOverviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A5A))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📊 LAUF-STATISTIKEN",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFFF)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem("🏃", "Gesamt Distanz", "47.2 km", Color(0xFFFF6B6B))
                    StatItem("⏱️", "Gesamt Zeit", "6h 32m", Color(0xFF4ECDC4))
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem("⚡", "Ø Geschwindigkeit", "7.2 km/h", Color(0xFFFFE66D))
                    StatItem("🔥", "Kalorien", "3,420", Color(0xFFFF6B9D))
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem("🎯", "Erfolgsrate", "85%", Color(0xFF95E1D3))
                    StatItem("📅", "Aktive Tage", "23", Color(0xFFA8E6CF))
                }
            }
        }
    }
}

@Composable
fun AchievementsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A2A0F))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🏆 ERRUNGENSCHAFTEN",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AchievementItem("🥇", "ERSTER SIEG", "Erste Challenge abgeschlossen", true)
                AchievementItem("🏃‍♂️", "LÄUFER", "10 km gelaufen", true)
                AchievementItem("⚡", "SPEEDSTER", "12 km/h erreicht", true)
                AchievementItem("🎯", "PERFEKTIONIST", "5 Challenges perfekt", false)
                AchievementItem("🔥", "MARATHON-HELD", "21 km am Stück", false)
            }
        }
    }
}

@Composable
fun RecentActivityCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1A4A))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📈 LETZTE AKTIVITÄTEN",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9C27B0)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActivityItem("🏃‍♂️", "ANFÄNGER JOGGER", "Erfolgreich • +30 XP", "Heute 14:23")
                ActivityItem("🦆", "NATUR-GENIEßER", "Erfolgreich • +35 XP", "Gestern 16:45")
                ActivityItem("⚡", "AUSDAUER-LÄUFER", "Nicht geschafft", "Gestern 10:30")
                ActivityItem("🌱", "PARK-ERKUNDER", "Erfolgreich • +40 XP", "Vor 2 Tagen")
            }
        }
    }
}

@Composable
fun QuickStatItem(icon: String, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCCCCCC)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun StatItem(icon: String, label: String, value: String, color: Color) {
    Column(
        modifier = Modifier.width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFCCCCCC),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AchievementItem(icon: String, title: String, description: String, isUnlocked: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isUnlocked) icon else "🔒",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) Color.White else Color(0xFF666666)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isUnlocked) Color(0xFFCCCCCC) else Color(0xFF444444)
            )
        }
        if (isUnlocked) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF00FF00)
            )
        }
    }
}

@Composable
fun ActivityItem(icon: String, title: String, result: String, timestamp: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = result,
                style = MaterialTheme.typography.bodySmall,
                color = if (result.contains("Erfolgreich")) Color(0xFF00FF00) else Color(0xFF666666)
            )
        }
        Text(
            text = timestamp,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF999999)
        )
    }
} 