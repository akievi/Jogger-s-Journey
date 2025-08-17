package com.example.joggersjourney.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(navController: NavController) {
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
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Gaming-Style 2x2 Grid
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Erste Reihe: Avatar und Einstellungen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Avatar Bereich - Gaming Style
                GamingProfilCard(
                    title = "AVATAR",
                    icon = Icons.Default.Person,
                    iconText = "👤",
                    backgroundColor = Color(0xFF6600CC), // Lila
                    accentColor = Color(0xFF9966FF),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("avatar") }
                )
                
                // Einstellungen Bereich - Gaming Style
                GamingProfilCard(
                    title = "EINSTELLUNGEN",
                    icon = Icons.Default.Settings,
                    iconText = "⚙️",
                    backgroundColor = Color(0xFF0066CC), // Blau
                    accentColor = Color(0xFF3399FF),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("settings") }
                )
            }
            
            // Zweite Reihe: Shop und Social
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Shop Bereich - Gaming Style
                GamingProfilCard(
                    title = "SHOP",
                    icon = Icons.Default.ShoppingCart,
                    iconText = "🛒",
                    backgroundColor = Color(0xFFCC6600), // Orange
                    accentColor = Color(0xFFFF9933),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("shop") }
                )
                
                // Social Bereich - Gaming Style
                GamingProfilCard(
                    title = "SOCIAL",
                    icon = Icons.Default.Person,
                    iconText = "👥",
                    backgroundColor = Color(0xFF00CC66), // Grün
                    accentColor = Color(0xFF33FF99),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("social") }
                )
            }
        }
    }
}

@Composable
fun GamingProfilCard(
    title: String,
    icon: ImageVector,
    iconText: String,
    backgroundColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f), // Quadratisch
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A4A)), // Dunkler Gaming-Hintergrund
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp) // Für Border-Effekt
        ) {
            // Gaming-Style Gradient Effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        backgroundColor.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Gaming-Style Icon Circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    // Emoji Icon
                    Text(
                        text = iconText,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Gaming-Style Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                
                // Gaming-Style Subtitle
                Text(
                    text = "[ ZUGRIFF ]",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFCCCCCC)
                )
            }
            
            // Gaming-Style Corner Accent
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .background(
                        accentColor,
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
} 