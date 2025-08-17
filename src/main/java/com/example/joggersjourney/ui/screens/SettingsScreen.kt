package com.example.joggersjourney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var soundEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E8))
    ) {
        // Top Bar mit Zurück-Button
        TopAppBar(
            title = { Text("Einstellungen", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFE8F5E8)
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingsSection(title = "Audio")
            }
            
            item {
                SettingsItem(
                    title = "Sound-Effekte",
                    subtitle = "Quest-Sounds und UI-Feedback",
                    icon = Icons.Default.Add,
                    trailing = {
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it }
                        )
                    }
                )
            }
            
            item {
                SettingsSection(title = "Benachrichtigungen")
            }
            
            item {
                SettingsItem(
                    title = "Push-Benachrichtigungen",
                    subtitle = "Neue Quests und Achievements",
                    icon = Icons.Default.Notifications,
                    trailing = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }
                )
            }
            
            item {
                SettingsSection(title = "Aussehen")
            }
            
            item {
                SettingsItem(
                    title = "Dunkler Modus",
                    subtitle = "Dark Theme aktivieren",
                    icon = Icons.Default.Build,
                    trailing = {
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it }
                        )
                    }
                )
            }
            
            item {
                SettingsSection(title = "Account")
            }
            
            item {
                SettingsItem(
                    title = "Profil bearbeiten",
                    subtitle = "Name und Avatar ändern",
                    icon = Icons.Default.Edit,
                    onClick = { /* TODO: Profil bearbeiten */ }
                )
            }
            
            item {
                SettingsItem(
                    title = "Datenschutz",
                    subtitle = "Datenverwendung und Privatsphäre",
                    icon = Icons.Default.Settings,
                    onClick = { /* TODO: Datenschutz */ }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            item {
                SettingsItem(
                    title = "App Version",
                    subtitle = "1.0.0 (Beta)",
                    icon = Icons.Default.Info,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2E7D32),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            
            trailing?.invoke()
        }
    }
} 