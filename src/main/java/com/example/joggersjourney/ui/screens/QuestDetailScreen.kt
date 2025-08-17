package com.example.joggersjourney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.joggersjourney.data.*
import com.example.joggersjourney.data.models.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(questId: String, navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Managers
    val audioManager = remember { AudioManager(context) }
    val challengeManager = remember { ChallengeManager(audioManager) }
    val locationService = remember { LocationService(context) }
    
    // Quest data
    val quest = remember { QuestRepository.getQuestById(questId) }
    
    // UI State
    var showingIntroDialog by remember { mutableStateOf(true) }
    var showingResultDialog by remember { mutableStateOf(false) }
    var challengeResult by remember { mutableStateOf<ChallengeResult?>(null) }
    var currentNpcMessage by remember { mutableStateOf("") }
    var isSimulationMode by remember { mutableStateOf(false) }
    var simulationSpeed by remember { mutableStateOf(2.1) }
    
    // Challenge tracking state
    val currentSession by challengeManager.currentSession.collectAsState()
    val elapsedTime by challengeManager.elapsedTime.collectAsState()
    val currentSpeed by challengeManager.currentSpeed.collectAsState()
    val distanceCovered by challengeManager.distanceCovered.collectAsState()
    
    // Location tracking
    LaunchedEffect(currentSession) {
        if (currentSession?.isActive == true) {
            // Start location tracking
            locationService.getLocationUpdates().collect { location ->
                challengeManager.updateLocation(location.latitude, location.longitude)
            }
        }
    }
    
    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            audioManager.release()
            locationService.disableSimulationMode()
        }
    }
    
    if (quest == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Quest nicht gefunden!", color = Color.Red)
        }
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    quest.title, 
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ) 
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF0F0F23)
            )
        )
        
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Quest Info Card
            QuestInfoCard(quest)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // NPC Character Card
            NPCCard(quest, currentNpcMessage)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Challenge Tracking (only visible when active)
            if (currentSession?.isActive == true) {
                ChallengeTrackingCard(
                    quest = quest,
                    elapsedTime = elapsedTime,
                    currentSpeed = currentSpeed,
                    distanceCovered = distanceCovered,
                    challengeManager = challengeManager
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Complete/Cancel buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                challengeResult = challengeManager.completeChallenge(quest)
                                showingResultDialog = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00))
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BEENDEN")
                    }
                    
                    Button(
                        onClick = {
                            challengeManager.cancelChallenge()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCC0000))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ABBRECHEN")
                    }
                }
            } else {
                // Start Challenge Button
                Button(
                    onClick = {
                        scope.launch {
                            if (challengeManager.startChallenge(quest)) {
                                showingIntroDialog = false
                                currentNpcMessage = quest.npcDialogueIntro.randomOrNull() ?: ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6600CC))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "CHALLENGE STARTEN",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Add Simulation Controls
            if (currentSession?.isActive == true) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A4A))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "🎮 SIMULATION",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00FFFF)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Simulation:",
                                color = Color.White
                            )
                            Switch(
                                checked = isSimulationMode,
                                onCheckedChange = { enabled ->
                                    isSimulationMode = enabled
                                    if (enabled) {
                                        locationService.enableSimulationMode()
                                        locationService.startSimulation(simulationSpeed)
                                    } else {
                                        locationService.disableSimulationMode()
                                    }
                                }
                            )
                        }
                        
                        if (isSimulationMode) {
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Simulation Speed: ${String.format("%.1f", simulationSpeed)} m/s (${String.format("%.1f", simulationSpeed * 3.6)} km/h)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            Slider(
                                value = simulationSpeed.toFloat(),
                                onValueChange = { simulationSpeed = it.toDouble() },
                                valueRange = 1.0f..5.0f,
                                steps = 39
                            )
                        }
                    }
                }
            }
        }
    }
    
    // NPC Intro Dialog
    if (showingIntroDialog && quest.npcDialogueIntro.isNotEmpty()) {
        NPCDialogScreen(
            npcName = quest.npcName,
            message = quest.npcDialogueIntro.random(),
            onDismiss = { showingIntroDialog = false }
        )
    }
    
    // Challenge Result Dialog
    if (showingResultDialog && challengeResult != null) {
        ChallengeResultDialog(
            result = challengeResult!!,
            quest = quest,
            onDismiss = { 
                showingResultDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun QuestInfoCard(quest: Quest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A4A))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                quest.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Quest Requirements
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (quest.targetDistance > 0) {
                    QuestRequirement(
                        icon = "🏃",
                        label = "Distanz",
                        value = "${quest.targetDistance} km"
                    )
                }
                if (quest.targetTime > 0) {
                    QuestRequirement(
                        icon = "⏱️",
                        label = "Zeit",
                        value = "${quest.targetTime} min"
                    )
                }
                if (quest.requiredSpeed > 0) {
                    QuestRequirement(
                        icon = "⚡",
                        label = "Tempo",
                        value = "${String.format("%.1f", quest.requiredSpeed)} m/s"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Rewards
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RewardItem("💰", "${quest.rewardGold} Gold")
                RewardItem("⭐", "${quest.rewardXP} XP")
            }
        }
    }
}

@Composable
fun NPCCard(quest: Quest, currentMessage: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A5A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // NPC Avatar/Icon - größer und zentriert
            Text(
                text = when(quest.category) {
                    QuestCategory.EASY -> "🏃‍♂️"
                    QuestCategory.MEDIUM -> "⚡"
                    QuestCategory.HARD -> "🔥"
                    QuestCategory.EXPERT -> "👑"
                },
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // NPC Name - perfekt zentriert
            Text(
                text = quest.npcName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFFF),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            if (currentMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Message Card - zentriert mit mehr Padding
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A3A))
                ) {
                    Text(
                        text = currentMessage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ChallengeTrackingCard(
    quest: Quest,
    elapsedTime: Long,
    currentSpeed: Double,
    distanceCovered: Double,
    challengeManager: ChallengeManager
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2A0F))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "🏃 CHALLENGE AKTIV",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF00)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Timer Display
            Text(
                challengeManager.formatTime(elapsedTime),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Geschwindigkeit",
                    value = challengeManager.formatSpeed(currentSpeed),
                    color = if (currentSpeed >= quest.requiredSpeed) Color(0xFF00FF00) else Color(0xFFFFAA00)
                )
                
                StatItem(
                    label = "Distanz",
                    value = challengeManager.formatDistance(distanceCovered),
                    color = if (distanceCovered >= quest.targetDistance) Color(0xFF00FF00) else Color.White
                )
            }
        }
    }
}

@Composable
fun QuestRequirement(icon: String, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCCCCCC))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RewardItem(icon: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = color, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCCCCCC))
    }
}

@Composable
fun NPCDialogScreen(
    npcName: String,
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A4A))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    npcName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00FFFF)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6600CC))
                ) {
                    Text("VERSTANDEN")
                }
            }
        }
    }
}

@Composable
fun ChallengeResultDialog(
    result: ChallengeResult,
    quest: Quest,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (result.completed) Color(0xFF0F3A0F) else Color(0xFF3A0F0F)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Result Icon
                Text(
                    if (result.completed) "🏆" else "😅",
                    style = MaterialTheme.typography.displayLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    if (result.completed) "CHALLENGE GESCHAFFT!" else "CHALLENGE NICHT GESCHAFFT",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (result.completed) Color(0xFF00FF00) else Color(0xFFFF6666)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (result.npcMessage.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A3A))
                    ) {
                        Text(
                            "\"${result.npcMessage}\"",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Zeit", color = Color(0xFFCCCCCC))
                        Text(
                            ChallengeManager(AudioManager(LocalContext.current)).formatTime(result.timeTaken),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Tempo", color = Color(0xFFCCCCCC))
                        Text(
                            ChallengeManager(AudioManager(LocalContext.current)).formatSpeed(result.averageSpeed),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Distanz", color = Color(0xFFCCCCCC))
                        Text(
                            ChallengeManager(AudioManager(LocalContext.current)).formatDistance(result.distance),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                if (result.completed) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RewardItem("💰", "${result.goldEarned} Gold")
                        RewardItem("⭐", "${result.xpEarned} XP")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6600CC))
                ) {
                    Text("WEITER")
                }
            }
        }
    }
} 